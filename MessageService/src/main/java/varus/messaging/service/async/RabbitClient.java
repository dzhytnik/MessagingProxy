package varus.messaging.service.async;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        channel.queueDeclare(VARUS_MESSAGING_QUEUE, false, false, false, null);
    }

    @Override
    public void sendJMSMessage(String message) {
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.basicPublish("", VARUS_MESSAGING_QUEUE, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
