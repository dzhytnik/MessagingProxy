package varus.messaging.service.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import varus.messaging.dao.bean.ClientConfig;
import varus.messaging.dao.bean.Config;
import varus.messaging.dao.bean.MessageLogRecord;
import varus.messaging.service.MessagingServiceAppState;
import varus.messaging.service.bean.MessageDTO;
import varus.messaging.service.bean.MessageProviderResponse;
import varus.messaging.service.dao.ClientConfigRepository;
import varus.messaging.service.dao.ConfigRepository;
import varus.messaging.service.dao.MessageLogRepository;
import varus.messaging.service.dao.ProviderRepository;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitConsumer implements JMSConsumer {
    @Autowired
    private MessageLogRepository messageLogRepository;

    @Autowired
    MessageSenderWorker messageSender;

    @Autowired
    ClientConfigRepository clientConfigRepository;

    @Autowired
    ConfigRepository configRepository;

    @Autowired
    ProviderRepository providerRepository;

    @Autowired
    RabbitClient jmsClient;

    @Autowired
    MessagingServiceAppState appState;

    private static final String QUEUE_NAME = "varus-messaging-queue";
    private static final String REPORT_QUEUE_NAME = "varus-messaging-report--queue";

    @Override
    public void runConsumer() throws InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();

            Map<String, Object> props = new HashMap<>();
            props.put("x-max-priority", 3);

            channel.queueDeclare(QUEUE_NAME, false, false, false, props);
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }


    private DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        int priority = delivery.getProperties().getPriority();

        String message = new String(delivery.getBody(), "UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        MessageDTO messageDto = objectMapper.readValue(message, MessageDTO.class);

        switch (priority) {
            case JMSClient.HIGH_PRIORITY: case JMSClient.MEDIUM_PRIORITY:


                ClientConfig clientConfig = clientConfigRepository.findById(messageDto.getClientId()).get();
                Config config = configRepository.findById(1L).get();

                MessageProviderResponse messageSentStatus = messageSender.sendMessage(messageDto);


                if (messageSentStatus.getResponseCode() != HttpStatus.SC_OK) {

                    if (messageDto.getRetryCount() < clientConfig.getNumberOfAttempts()) {
                        //Increment retry counter for a provider. GLoиal retry count is specified for all providers.
                        //When this counter is reached provider should be switched to a reserve one
                        //I.e. main provider by default is Infobip, reserve is GMSu.
                        //After MaxRetryCount to Infobip we should switch to GMSu
                        if (appState.getCurrentProviderTryCount() < config.getNumberOfAttempts()) {
                            appState.incrCurrentProviderTryCount();
                        } else {
                            switchProvider();
                        }

                        //Increment retry count for this specific message. This is not related to current provider.
                        //Each client should have each own retry count
                        messageDto.incrRetryCount();

                        jmsClient.sendJMSMessage(objectMapper.writeValueAsString(messageDto), JMSClient.HIGH_PRIORITY);

                        messageLogRepository.save(new MessageLogRecord(messageDto.getRecepientList().get(0), messageDto.getMessageText(),
                                new Date(), messageSentStatus.getResponseCode(), "0"));


                    } else {
                        messageLogRepository.save(new MessageLogRecord(messageDto.getRecepientList().get(0), messageDto.getMessageText(),
                                new Date(), messageSentStatus.getResponseCode(), messageSentStatus.getMessageId()));
                    }

                    //TODO Acquire message delivery status
                } else {

                    if (messageSentStatus.getMessageId() != null && !messageSentStatus.getMessageId().isEmpty()) {
                        messageDto.setProviderId(appState.getCurrentProviderId());
                        messageDto.setMessageId(messageSentStatus.getMessageId());
                        jmsClient.sendJMSMessage(objectMapper.writeValueAsString(messageDto), JMSClient.LOW_PRIORITY);
                    }

                    if (appState.getCurrentProviderId() != config.getDefaultProviderId()) {

                        long milis = System.currentTimeMillis() - appState.getReserveProviderStartTime().getTime();
                        //SecondaryChannelTimeSlot - parameter in minutes to send into reserve channel
                        if (milis / 1000 / 60 > config.getSecondaryChannelTimeslot()) {
                            switchProvider();
                        }
                    }
                    messageLogRepository.save(new MessageLogRecord(messageDto.getRecepientList().get(0), messageDto.getMessageText(),
                            new Date(), messageSentStatus.getResponseCode(), messageSentStatus.getMessageId()));
                }
                break;
            case JMSClient.LOW_PRIORITY:
                messageSender.requestDeliveryReport(messageDto);
                break;
            default:
                break;


        }
    };

    private void switchProvider() {
        long providersCount = providerRepository.count();
        appState.setCurrentProviderId((appState.getCurrentProviderId() % providersCount) + 1);
        appState.setCurrentProviderTryCount(0L);
        appState.setReserveProviderStartTime(new Date());
    }

}
