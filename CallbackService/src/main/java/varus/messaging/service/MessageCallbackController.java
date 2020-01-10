package varus.messaging.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD2;
import varus.messaging.async.MessageSenderWorker;
import varus.messaging.service.bean.MessageDTO;
import varus.messaging.service.bean.MessagesWrapper;

import java.io.IOException;

@RestController
public class MessageCallbackController {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @RequestMapping("/callback")
    public String callback() {
        return "OK";
    }

    @PostMapping("/sendmessage/{clientId}")
    public String sendMessage(@RequestBody String message, @PathVariable int clientId) {


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

            MessageSenderWorker worker = new MessageSenderWorker("VARUS2_R", "dP2hch29", clientId);
            try {
                for (String phoneNumber : messageDto.getRecepientList()) {
                    int status = worker.sendMessage(messageDto.getMessageText(),
                            phoneNumber, 1);
                    if (status != 200) {
                        return "Failed to send a message. " + status;
                    }
                }
            } catch (UnirestException e) {
                return "Failed to send a message. " + e.getLocalizedMessage();
            }

        }
        return "OK";
    }

    @GetMapping("/getconfig")
    public String getConfig() {
        return providerRepository.findByProviderName("Infobip").getUsername();
    }
}
