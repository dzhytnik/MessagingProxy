package varus.messaging.async;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import varus.messaging.service.ConfigLoader;

import java.util.Base64;

public class MessageSenderWorker{
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

    public int sendMessage(String textToSend, String phoneNumber, int channelId) throws UnirestException{
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
                body.append("\"phone_number\": ").append(phoneNumber).append(LINE_SEPARATOR);

                //HttpResponse response = Unirest.post("https://api-v2.hyber.im/2157")
                HttpResponse response = Unirest.post(ConfigLoader.getInstance().getGmsuConfig().getPrimaryUrl())
                        .header("Content-Type", "application/json")
                        .header("Authorization", authorization)
                        .body(body.toString() + "extra_id\": \"AD-6640-7006\",\r\n\"callback_url\": \"https://send-dr-here.com\",\r\n\"start_time\": \"2019-12-28 10:52:00\",\r\n\"tag\": \"Campaign name\",\r\n\"channels\": [\r\n\"viber\",\r\n\"sms\"\r\n],\r\n\"channel_options\": {\r\n\"sms\": {\r\n\"text\":  " + textToSend + " ,\r\n\"alpha_name\": \"\",\r\n\"ttl\": 300\r\n},\r\n\"viber\": {\r\n\"text\": \"Text for Viber\",\r\n\"ttl\": 60,\r\n\"img\": \"http://olddogs.org/logo.png\",\r\n\"caption\": \"Old Dogs need you!\",\r\n\"action\": \"http://olddogs.org\"\r\n}\r\n}\r\n}")
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
