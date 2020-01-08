package varus.messaging.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import varus.messaging.async.MessageSenderWorker;

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

    @PostMapping("/sendmessage/{phoneNumber}/{channel}/{clientId}/{tag}")
    public String sendMessage(@RequestBody String message, @PathVariable String phoneNumber,
                              @PathVariable int channel, @PathVariable int clientId, @PathVariable(required=false) String tag) {

        MessageSenderWorker worker = new MessageSenderWorker("VARUS2_R", "dP2hch29", clientId);
        try {
            int status = worker.sendMessage(message, phoneNumber, channel);
            if (status == 200) {
                return "OK";
            } else {
                return "Failed to send a message. " + status;
            }
        } catch (UnirestException e) {
            return "Failed to send a message. " + e.getLocalizedMessage();
        }
    }

    @GetMapping("/getconfig")
    public String getConfig() {
        return providerRepository.findByProviderName("Infobip").getUsername();
    }
}
