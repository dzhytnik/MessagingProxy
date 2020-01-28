package varus.messaging.service.bean;

import java.util.List;

public class MessageDTO {
    Long clientId;
    Integer retryCount = 1;
    List<String> channels;
    List<String> recepientList;
    String messageText;
    String tag;
    String imageUrl;
    String buttonText;
    String buttonUrl;

    Long providerId;
    String messageId;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<String> getChannels() {
        return channels;
    }

    public List<String> getRecepientList() {
        return recepientList;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getTag() {
        return tag;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void incrRetryCount() {
        this.retryCount++;
    }
}

