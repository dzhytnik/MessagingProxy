package varus.messaging.service.async;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

@FunctionalInterface
public interface MessageSender {
    HttpResponse sendMessage() throws UnirestException;
}
