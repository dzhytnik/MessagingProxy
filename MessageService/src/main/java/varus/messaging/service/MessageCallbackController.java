package varus.messaging.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import varus.messaging.service.async.JMSClient;
import varus.messaging.service.async.RabbitClient;
import varus.messaging.service.bean.MessageDTO;
import varus.messaging.service.bean.MessagesWrapper;
import varus.messaging.service.dao.ConfigRepository;
import varus.messaging.service.dao.MessageLogRepository;
import varus.messaging.service.dao.ProviderRepository;

import java.io.IOException;

@RestController
@RequestMapping("/varusMessagingProxy/v1")
public class MessageCallbackController {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private MessageLogRepository messageLogRepository;

    @Autowired
    RabbitClient jmsClient;

    @PostMapping("/sendMessage/{clientId}")
    public String sendMessage(@RequestBody String message, @PathVariable long clientId) {
        //TODO: Add check for client id is present in the config db. Return error 400 if not



        MessagesWrapper messages = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            messages = objectMapper.readValue(message, MessagesWrapper.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (MessageDTO messageDto : messages.getMessages()) {
            messageDto.setClientId(clientId);
            jmsClient.sendJMSMessage(messageDto, JMSClient.HIGH_PRIORITY);
        }

        return "OK";


    }

}
