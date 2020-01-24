package varus.messaging.service.async;

public interface JMSClient {
    static final int LOW_PRIORITY = 1;
    static final int MEDIUM_PRIORITY = 2;
    static final int HIGH_PRIORITY = 3;
    void sendJMSMessage(String messagem, int priority);
}
