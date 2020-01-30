package varus.messaging.service.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.util.concurrent.*;

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
    private static final String AD_QUEUE_NAME = "varus-messaging-ad-queue";

    String adChannelConsumer;

    ConnectionFactory factory = null;
    Connection connection = null;
    Channel channel = null;

    public RabbitConsumer() {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        try {
            runConsumer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void runConsumer() throws InterruptedException {
        try {
            channel = connection.createChannel();

            Map<String, Object> props = new HashMap<>();
            props.put("x-max-priority", 3);

            channel.queueDeclare(RabbitClient.QUEUE_NAME, false, false, false, props);
            channel.basicConsume(RabbitClient.QUEUE_NAME, true, deliverCallback, consumerTag -> { });

            channel.queueDeclare(RabbitClient.AD_QUEUE_NAME, false, false, false, props);
            adChannelConsumer = channel.basicConsume(RabbitClient.AD_QUEUE_NAME, true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Runs in scheduled time. Used for advertisement messages which are not good to be sent
    //outside the specified time frame. This time frame is configured in the DB
    //ad_time_window_start and ad_time_window_end are the parameters for this
    //These are the CRON formatted lines, see https://www.baeldung.com/cron-expressions for more detailes
    @Scheduled(cron = "#{@applicationPropertyService.getTimeWindowEnd()}")
    public void runConsumerForAdMessages(){
        try {
            System.out.println("Enabling ad messaging");
            adChannelConsumer = channel.basicConsume(RabbitClient.AD_QUEUE_NAME, true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Scheduled(cron = "#{@applicationPropertyService.getTimeWindowStart()}")
    public void shutdouwnConsumerForAdMessages(){
        try {
            System.out.println("Disabling ad messaging");
            channel.basicCancel(adChannelConsumer);
        } catch (IOException e) {
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


                if (messageSentStatus.getResponseCode() != HttpStatus.SC_OK || messageSentStatus.getStatusCode() != 1) {

                    if (messageDto.getRetryCount() < clientConfig.getNumberOfAttempts()) {
                        //Increment retry counter for a provider. Global retry count is specified for all providers.
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

                        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                        scheduler.schedule(new Callable<Integer>() {
                            @Override
                            public Integer call() throws Exception {

                                jmsClient.sendJMSMessage(messageDto, JMSClient.HIGH_PRIORITY);
                                return null;
                            }
                        }, 2, TimeUnit.SECONDS);




                        messageLogRepository.save(new MessageLogRecord(messageDto.getRecepientList().get(0), messageDto.getMessageText(),
                                new Date(), messageSentStatus.getResponseCode(), "0"));


                    } else {
                        messageLogRepository.save(new MessageLogRecord(messageDto.getRecepientList().get(0), messageDto.getMessageText(),
                                new Date(), messageSentStatus.getResponseCode(), messageSentStatus.getMessageId()));
                    }


                } else {

                    //If we get the messageId which means the message was sent we need to get a report
                    //LOW priority queue is responsible for reports
                    if (messageSentStatus.getMessageId() != null && !messageSentStatus.getMessageId().isEmpty()) {
                        messageDto.setProviderId(appState.getCurrentProviderId());
                        messageDto.setMessageId(messageSentStatus.getMessageId());
                        jmsClient.sendJMSMessage(messageDto, JMSClient.LOW_PRIORITY);
                    }


                    //If reserve provider is used we need to countdown a timeout
                    if (appState.getCurrentProviderId() != config.getDefaultProviderId()) {

                        //Switching back to the main provider after timeout reached.
                        long milis = System.currentTimeMillis() - appState.getReserveProviderStartTime().getTime();
                        //SecondaryChannelTimeSlot - parameter in minutes to send into main channel
                        if (milis / 1000 / 60 >= config.getSecondaryChannelTimeslot()) {
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
