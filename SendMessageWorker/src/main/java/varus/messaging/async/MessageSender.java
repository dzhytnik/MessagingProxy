package varus.messaging.async;

import com.mashape.unirest.http.exceptions.UnirestException;

@FunctionalInterface
public interface MessageSender {
    void sendMessage() throws UnirestException;
}
