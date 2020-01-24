package varus.messaging.service.async;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitClient implements JMSClient{
    public static final String VARUS_MESSAGING_QUEUE = "varus-messaging-queue";
    ConnectionFactory factory;

    private RabbitClient()  throws Exception{
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> props = new HashMap<>();
        props.put("x-max-priority", 3);

        channel.queueDeclare(VARUS_MESSAGING_QUEUE, false, false, false, props);
    }

    @Override
    public void sendJMSMessage(String message, int priority) {
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            AMQP.BasicProperties.Builder basicProps = new AMQP.BasicProperties.Builder();
            basicProps.contentType("text/plain")
                    .priority(priority);

            channel.basicPublish("", VARUS_MESSAGING_QUEUE, basicProps.build(), message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void sendJMSMessage(String message) {
        sendJMSMessage(message, JMSClient.LOW_PRIORITY);
    }

}
