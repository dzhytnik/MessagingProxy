package varus.messaging.service.async;

public interface JMSClient {
    static final int LOW_PRIORITY = 1;
    static final int HIGH_PRIORITY = 1;
    void sendJMSMessage(String messagem, int priority);
}
