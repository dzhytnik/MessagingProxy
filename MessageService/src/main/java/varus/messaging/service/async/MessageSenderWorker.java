package varus.messaging.service.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import varus.messaging.service.ConfigLoader;
import varus.messaging.service.MessagingServiceAppState;
import varus.messaging.service.bean.GMSu.ChannelMessage;
import varus.messaging.service.bean.GMSu.ChannelOptions;
import varus.messaging.service.bean.GMSu.GMSuMessage;
import varus.messaging.service.bean.InfoBip.*;
import varus.messaging.service.bean.MessageDTO;
import varus.messaging.service.bean.MessageProviderResponse;
import varus.messaging.service.bean.common.BaseProviderMessage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Component
public class MessageSenderWorker implements MessageSender{
    @Autowired
    ConfigLoader configLoader;

    @Autowired
    MessagingServiceAppState appState;

    public static final int INFOBIP_CHANNEL = 1;
    public static final int GMSU_CHANNEL = 2;


    private static final String LINE_SEPARATOR = ",\r\n";

    private int clientId;


    public MessageProviderResponse sendMessage(MessageDTO messageDTO) throws UnirestException{
        String textToSend = messageDTO.getMessageText();
        String phoneNumber = messageDTO.getRecepientList().get(0);

        long channelId = appState.getCurrentProviderId();

        MessageSender sender = null;
        HttpResponse response = null;
        String messageId = "";


        if (channelId == GMSU_CHANNEL) {
            String username = configLoader.getGmsuConfig().getUsername();
            String password = configLoader.getGmsuConfig().getUserPassword();
            String authorization = username + ":" + password;
            String encodedBytes = Base64.getEncoder().encodeToString(authorization.getBytes());
            authorization = "Basic " + encodedBytes;


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();

            GMSuMessage.GMSuMessageBuilder gmsuMessageBuilder = GMSuMessage.builder();
            BaseProviderMessage providerMessage = gmsuMessageBuilder
                    .phoneNumber(phoneNumber)
                    .startTime(formatter.format(date))
                    .tag(messageDTO.getTag())
                    .channelOptions(
                            ChannelOptions.builder()
                                    .sms(ChannelMessage.builder().text(textToSend).build())
                                    .viber(ChannelMessage.builder().text(textToSend).build())
                            .build())
                    .channels(messageDTO.getChannels())
                    .build();
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                response = Unirest.post(configLoader.getGmsuConfig().getPrimaryUrl())
                        .header("Content-Type", "application/json")
                        .header("Authorization", authorization)
                        .body(objectMapper.writeValueAsString(providerMessage))
                        .asString();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } else if (channelId == INFOBIP_CHANNEL){
            InfoBipMessage infoBipMessage = InfoBipMessage.builder().destination(
                    Destination.builder().to(To.builder().phoneNumber(phoneNumber).build())
                            .build()).sms(TextMessage.builder().text(textToSend).build())
                    .viber(TextMessage.builder().text(textToSend).build())
                    .scenarioKey("6CB8B6B51D49EA3049A0FA7BCA94AD51").build();
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                response = Unirest.post(configLoader.getInfobipConfig().getPrimaryUrl())
                        .header("Content-Type", "application/json")
                        .header("Authorization", "App c7bb509341ec624a0a41de7754356208-7d2e4651-6117-4a75-be96-2ceb5683fc72")
                        .body(objectMapper.writeValueAsString(infoBipMessage))
                        .asString();
                OmniResponse omniResponse = objectMapper.readValue(response.getBody().toString(), OmniResponse.class);
                messageId = omniResponse.getMessages().get(0).getMessageId();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return MessageProviderResponse.builder().responseCode(response.getStatus()).messageId(messageId).build();
    }
}
