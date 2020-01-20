package varus.messaging.service.async;

public interface JMSClient {
    void sendJMSMessage(String message);
}
