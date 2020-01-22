package varus.messaging.service.async;

import com.mashape.unirest.http.exceptions.UnirestException;
import varus.messaging.service.bean.MessageDTO;
import varus.messaging.service.bean.MessageProviderResponse;

public interface MessageSender {
    MessageProviderResponse sendMessage(MessageDTO messageDTO) throws UnirestException;
}
