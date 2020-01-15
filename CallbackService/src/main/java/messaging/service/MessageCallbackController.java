package messaging.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import messaging.async.JMSClient;
import messaging.service.bean.MessageDTO;
import messaging.service.bean.MessagesWrapper;

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

    @RequestMapping("/callback")
    public String callback() {
        return "OK";
    }

    @PostMapping("/sendMessage/{clientId}")
    public String sendMessage(@RequestBody String message, @PathVariable int clientId) {
        //TODO: Add check for client id is present in the config db. Return error 400 if not



        MessagesWrapper messages = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            messages = objectMapper.readValue(message, MessagesWrapper.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (MessageDTO messageDto : messages.getMessages()) {
            if (messageDto.getChannels().size() != 1 || !"sms".equals(messageDto.getChannels().get(0))) {
                return "Incorrect channel. Only SMS supported";
            }

            try {
                messageDto.setClientId(clientId);
                JMSClient.getInstance().sendJMSMessage(objectMapper.writeValueAsString(messageDto));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

/*
            MessageSenderWorker worker = new MessageSenderWorker("VARUS2_R", "dP2hch29", clientId);
            try {
                for (String phoneNumber : messageDto.getRecepientList()) {
                    JMSClient.getInstance().sendJMSMessage(messageDto.getMessageText());
                    int status = worker.sendMessage(messageDto.getMessageText(),
                            phoneNumber, 1);
                    if (status != 200) {
                        messageLogRepository.save(new MessageLogRecord(messageDto.getRecepientList().get(0), messageDto.getMessageText(),
                                new Date(), status, "0"));
                        return "Failed to send a message. " + status;
                    }
                    messageLogRepository.save(new MessageLogRecord(messageDto.getRecepientList().get(0), messageDto.getMessageText(),
                            new Date(), 200, ""));
                }
            } catch (UnirestException e) {
                messageLogRepository.save(new MessageLogRecord(messageDto.getRecepientList().get(0), messageDto.getMessageText(),
                        new Date(), -1, "0"));
                return "Failed to send a message. " + e.getLocalizedMessage();
            }
*/

        }
        return "OK";
    }

    @GetMapping("/getconfig")
    public String getConfig() {
        return providerRepository.findByProviderName("Infobip").getUsername();
    }
}