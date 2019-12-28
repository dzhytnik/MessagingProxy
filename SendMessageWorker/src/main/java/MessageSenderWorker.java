import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Base64;

public class MessageSenderWorker{
    public static final int GMSU_CHANNEL = 1;
    public static final int INFOBIP_CHANNEL = 2;

    private String username;
    private String password;
    private String authorization;

    public MessageSenderWorker(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendMessage(int channelId) throws UnirestException{
        MessageSender sender = null;
        if (channelId == GMSU_CHANNEL) {
            sender = () -> {
                String encodedBytes;
                if (username != null && password != null) {
                    authorization = username + ":" + password;
                    encodedBytes = Base64.getEncoder().encodeToString(authorization.getBytes());
                    authorization = "Basic " + encodedBytes;
                }

                HttpResponse response = Unirest.post("https://api-v2.hyber.im/2157")
                        .header("Content-Type", "application/json")
                        .header("Authorization", authorization)
                        .body("{\"phone_number\": 380673980782,\r\n\"extra_id\": \"AD-6640-7006\",\r\n\"callback_url\": \"https://send-dr-here.com\",\r\n\"start_time\": \"2019-12-28 10:52:00\",\r\n\"tag\": \"Campaign name\",\r\n\"channels\": [\r\n\"viber\",\r\n\"sms\"\r\n],\r\n\"channel_options\": {\r\n\"sms\": {\r\n\"text\": \"Text for SMS\",\r\n\"alpha_name\": \"\",\r\n\"ttl\": 300\r\n},\r\n\"viber\": {\r\n\"text\": \"Text for Viber\",\r\n\"ttl\": 60,\r\n\"img\": \"http://olddogs.org/logo.png\",\r\n\"caption\": \"Old Dogs need you!\",\r\n\"action\": \"http://olddogs.org\"\r\n}\r\n}\r\n}")
                        .asString();
            };

        } else if (channelId == INFOBIP_CHANNEL){
            sender = () -> {

            };
        }

        sender.sendMessage();

    }
}
