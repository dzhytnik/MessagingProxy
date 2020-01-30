package varus.messaging.service.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import varus.messaging.service.bean.MessageDTO;
import varus.messaging.service.dao.ClientConfigRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitClient implements JMSClient{
    @Autowired
    ClientConfigRepository clientConfigRepository;

    public static final String QUEUE_NAME = "varus-messaging-queue";
    public static final String AD_QUEUE_NAME = "varus-messaging-ad-queue";
    ConnectionFactory factory;

    private RabbitClient()  throws Exception{
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> props = new HashMap<>();
        props.put("x-max-priority", 3);

        channel.queueDeclare(QUEUE_NAME, false, false, false, props);
        channel.queueDeclare(AD_QUEUE_NAME, false, false, false, props);
    }

    @Override
    public void sendJMSMessage(MessageDTO messageDTO, int priority) {
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            AMQP.BasicProperties.Builder basicProps = new AMQP.BasicProperties.Builder();
            basicProps.contentType("text/plain")
                    .priority(priority);

            ObjectMapper objectMapper = new ObjectMapper();
            if (clientConfigRepository.findById(messageDTO.getClientId()).get().getTimeWindowRestricted()) {
                channel.basicPublish("", AD_QUEUE_NAME, basicProps.build(), objectMapper.writeValueAsString(messageDTO).getBytes());
            } else {
                channel.basicPublish("", QUEUE_NAME, basicProps.build(), objectMapper.writeValueAsString(messageDTO).getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void sendJMSMessage(MessageDTO messageDTO) {
        sendJMSMessage(messageDTO, JMSClient.LOW_PRIORITY);
    }

}
