package varus.messaging.service.async;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import varus.messaging.service.ConfigLoader;
import varus.messaging.service.bean.MessageDTO;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Component
public class MessageSenderWorker{
    @Autowired
    ConfigLoader configLoader;

    public static final int GMSU_CHANNEL = 1;
    public static final int INFOBIP_CHANNEL = 2;


    private static final String LINE_SEPARATOR = ",\r\n";

    private String username;
    private String password;
    private String authorization;
    private int clientId;

    public MessageSenderWorker(String username, String password, int clientId) {
        this.username = username;
        this.password = password;
        this.clientId = clientId;
    }

    public int sendMessage(MessageDTO messageDTO) throws UnirestException{
        String textToSend = messageDTO.getMessageText();
        String phoneNumber = messageDTO.getRecepientList().get(0);
        int channelId = 1;//configLoader.getConfig().

        MessageSender sender = null;
        String encodedBytes;
        if (username != null && password != null) {
            authorization = username + ":" + password;
            encodedBytes = Base64.getEncoder().encodeToString(authorization.getBytes());
            authorization = "Basic " + encodedBytes;
        }


        if (channelId == GMSU_CHANNEL) {
            sender = () -> {

                StringBuilder body = new StringBuilder("{");
                body.append("\"phone_number\": \"").append(phoneNumber).append("\"").append(LINE_SEPARATOR);

                //HttpResponse response = Unirest.post("https://api-v2.hyber.im/2157")
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();

                HttpResponse response = Unirest.post(ConfigLoader.getInstance().getGmsuConfig().getPrimaryUrl())
                        .header("Content-Type", "application/json")
                        .header("Authorization", authorization)
                        .body(body.toString() + "\"extra_id\": \"AD-6640-7006\",\r\n\"callback_url\": \"https://send-dr-here.com\",\r\n\"start_time\": \"  " +
                                formatter.format(date) +
                                "\",\r\n\"tag\": \"Campaign name\",\r\n\"channels\": [\"sms\"\r\n],\r\n\"channel_options\": {\r\n\"sms\": {\r\n\"text\":  \"" + textToSend + "\" ,\r\n\"alpha_name\": \"\",\r\n\"ttl\": 300\r\n}\r\n}\r\n}")
                        .asString();
                return response;
            };

        } else if (channelId == INFOBIP_CHANNEL){
            sender = () -> {

                return null;
            };
        }

        return sender.sendMessage().getStatus();

    }
}
