package varus.messaging.service.bean;

import java.util.List;

public class MessageDTO {
    Integer clientId;
    List<String> channels;
    List<String> recepientList;
    String messageText;
    String tag;
    String imageUrl;
    String buttonText;
    String buttonUrl;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
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
}

