package MessageCallbackService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageCallbackController {
    @RequestMapping("/callback")
    public String callback() {
        return "OK";
    }
}
