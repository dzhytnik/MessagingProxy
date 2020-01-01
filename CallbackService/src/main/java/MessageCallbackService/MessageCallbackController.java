package MessageCallbackService;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.web.bind.annotation.*;
import varus.messaging.async.MessageSenderWorker;

@RestController
public class MessageCallbackController {
    @RequestMapping("/callback")
    public String callback() {
        return "OK";
    }

    @PostMapping("/sendmessage/{phoneNumber}/{channel}/{clientId}")
    public String sendMessage(@RequestBody String message, @PathVariable String phoneNumber, @PathVariable int channel, @PathVariable int clientId) {

        MessageSenderWorker worker = new MessageSenderWorker("VARUS2_R", "dP2hch29", clientId);
        try {
            worker.sendMessage(message, phoneNumber, channel);
            return "OK";
        } catch (UnirestException e) {
            return "Failed to send a message";
        }
    }
}
