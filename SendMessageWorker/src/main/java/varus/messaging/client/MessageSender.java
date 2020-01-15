package varus.messaging.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

@FunctionalInterface
public interface MessageSender {
    HttpResponse sendMessage() throws UnirestException;
}
