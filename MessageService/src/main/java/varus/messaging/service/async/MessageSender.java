package varus.messaging.service.async;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import varus.messaging.service.bean.MessageDTO;
import varus.messaging.service.bean.MessageSentStatus;

public interface MessageSender {
    MessageSentStatus sendMessage(MessageDTO messageDTO) throws UnirestException;
}
