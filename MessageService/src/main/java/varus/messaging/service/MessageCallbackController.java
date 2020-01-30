package varus.messaging.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import varus.messaging.service.async.JMSClient;
import varus.messaging.service.async.RabbitClient;
import varus.messaging.service.bean.MessageDTO;
import varus.messaging.service.bean.MessagesWrapper;
import varus.messaging.service.dao.ClientConfigRepository;
import varus.messaging.service.dao.ConfigRepository;
import varus.messaging.service.dao.MessageLogRepository;
import varus.messaging.service.dao.ProviderRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

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
    private ClientConfigRepository clientConfigRepository;

    @Autowired
    RabbitClient jmsClient;

    @PostMapping("/sendMessage/{clientId}")
    public ResponseEntity<String> sendMessage(@RequestBody String message, @PathVariable long clientId) {
        //TODO: Add check for client id is present in the config db. Return error 400 if not

        if (!clientConfigRepository.findById(clientId).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Client ID is not registered");
        }



        MessagesWrapper messages = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            messages = objectMapper.readValue(message, MessagesWrapper.class);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect input");
        }


        StringBuffer returnGuids = new StringBuffer();
        for (MessageDTO messageDto : messages.getMessages()) {
            for (String channel : messageDto.getChannels()) {
                if (!Arrays.asList(new String[]{"viber", "sms"}).contains(channel)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect channel");
                }
            }
            messageDto.setClientId(clientId);
            String guid = UUID.randomUUID().toString();
            messageDto.setInternalMessageId(guid);
            returnGuids.append(guid).append(";");
            jmsClient.sendJMSMessage(messageDto, JMSClient.HIGH_PRIORITY);
        }

        return ResponseEntity.status(HttpStatus.OK).body(returnGuids.toString());


    }

}
