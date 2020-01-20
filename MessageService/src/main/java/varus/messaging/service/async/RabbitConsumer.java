package varus.messaging.service.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import varus.messaging.dao.bean.ClientConfig;
import varus.messaging.service.bean.MessageDTO;
import varus.messaging.service.bean.MessageSentStatus;
import varus.messaging.service.dao.ClientConfigRepository;
import varus.messaging.service.dao.MessageLogRepository;

import java.io.IOException;
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
    RabbitClient jmsClient;


    private static final String QUEUE_NAME = "varus-messaging-queue";

    @Override
    public void runConsumer() throws InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    private DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        MessageDTO messageDto = objectMapper.readValue(message, MessageDTO.class);
        try {
            MessageSentStatus messageSentStatus = messageSender.sendMessage(messageDto);
            if (messageSentStatus.getSentStatus() != HttpStatus.SC_OK) {

                //TODO Add configuration read from DB. Each client should have a parameter responsible for message sending retry count
                ClientConfig clientConfig = clientConfigRepository.findById(messageDto.getClientId()).get();
                if (clientConfig.getNumberOfAttempts() < messageDto.getRetryCount()) {
                    int retryCount = messageDto.getRetryCount();
                    retryCount++;
                    messageDto.setRetryCount(retryCount);
                    jmsClient.sendJMSMessage(objectMapper.writeValueAsString(messageDto));
                }

            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    };

}
