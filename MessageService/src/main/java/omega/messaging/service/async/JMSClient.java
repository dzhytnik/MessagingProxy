package varus.messaging.service.async;

import varus.messaging.service.bean.MessageDTO;

public interface JMSClient {
    static final int LOW_PRIORITY = 1;
    static final int MEDIUM_PRIORITY = 2;
    static final int HIGH_PRIORITY = 3;
    void sendJMSMessage(MessageDTO messageDTO, int priority);
}
