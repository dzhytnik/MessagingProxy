package varus.messaging.service.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import varus.messaging.service.ConfigLoader;
import varus.messaging.service.bean.InfoBip.Destination;
import varus.messaging.service.bean.InfoBip.InfoBipMessage;
import varus.messaging.service.bean.InfoBip.TextMessage;
import varus.messaging.service.bean.InfoBip.To;
import varus.messaging.service.bean.MessageDTO;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Component
public class MessageSenderWorker{
    @Autowired
    ConfigLoader configLoader;

    public static final int INFOBIP_CHANNEL = 1;
    public static final int GMSU_CHANNEL = 2;


    private static final String LINE_SEPARATOR = ",\r\n";

    private int clientId;


    public int sendMessage(MessageDTO messageDTO) throws UnirestException{
        String textToSend = messageDTO.getMessageText();
        String phoneNumber = messageDTO.getRecepientList().get(0);
        int channelId = configLoader.getConfig().getDefaultProviderId();

        MessageSender sender = null;


        if (channelId == GMSU_CHANNEL) {
            sender = () -> {

                String username = configLoader.getGmsuConfig().getUsername();
                String password = configLoader.getGmsuConfig().getUserPassword();
                String authorization = username + ":" + password;
                String encodedBytes = Base64.getEncoder().encodeToString(authorization.getBytes());
                authorization = "Basic " + encodedBytes;

                StringBuilder body = new StringBuilder("{");
                body.append("\"phone_number\": \"").append(phoneNumber).append("\"").append(LINE_SEPARATOR);

                //HttpResponse response = Unirest.post("https://api-v2.hyber.im/2157")
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();

                HttpResponse response = Unirest.post(configLoader.getGmsuConfig().getPrimaryUrl())
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
                InfoBipMessage infoBipMessage = InfoBipMessage.builder().destination(
                        Destination.builder().to(To.builder().phoneNumber(phoneNumber).build())
                                .build()).sms(TextMessage.builder().text(textToSend).build())
                        .viber(TextMessage.builder().text(textToSend).build())
                        .scenarioKey("6CB8B6B51D49EA3049A0FA7BCA94AD51").build();
                ObjectMapper objectMapper = new ObjectMapper();

                HttpResponse response = null;
                try {
                    response = Unirest.post(configLoader.getInfobipConfig().getPrimaryUrl())
                            .header("Content-Type", "application/json")
                            .header("Authorization", "App c7bb509341ec624a0a41de7754356208-7d2e4651-6117-4a75-be96-2ceb5683fc72")
                            .body(objectMapper.writeValueAsString(infoBipMessage))
                            .asString();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return response;
            };
        }

        return sender.sendMessage().getStatus();

    }
}
