package varus.messaging.service.async;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import varus.messaging.dao.bean.MessageReportRecord;
import varus.messaging.service.ConfigLoader;
import varus.messaging.service.MessagingServiceAppState;
import varus.messaging.service.bean.GMSu.ChannelMessage;
import varus.messaging.service.bean.GMSu.ChannelOptions;
import varus.messaging.service.bean.GMSu.GMSuMessage;
import varus.messaging.service.bean.GMSu.GMSuResponse;
import varus.messaging.service.bean.InfoBip.*;
import varus.messaging.service.bean.InfoBip.InfoBipReport.DeliveryReportResponse;
import varus.messaging.service.bean.InfoBip.InfoBipReport.SentSMSReport;
import varus.messaging.service.bean.MessageDTO;
import varus.messaging.service.bean.MessageProviderResponse;
import varus.messaging.service.bean.common.BaseProviderMessage;
import varus.messaging.service.dao.MessageReportRepository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


@Component
public class MessageSenderWorker extends BaseMessageSender{
    @Autowired
    ConfigLoader configLoader;

    @Autowired
    MessagingServiceAppState appState;

    @Autowired
    MessageReportRepository messageReportRepository;

    public static final int INFOBIP_CHANNEL = 1;
    public static final int GMSU_CHANNEL = 2;

    private final long INFOBIP_STATUS_PENDING = 1;
    private final long INFOBIP_STATUS_UNDELIVERABLE = 2;
    private final long INFOBIP_STATUS_DELIVERED = 3;
    private final long INFOBIP_STATUS_EXPIRED = 4;
    private final long INFOBIP_STATUS_REJECTED = 5;

    private int clientId;

    Logger logger = LoggerFactory.getLogger(MessageSenderWorker.class);



    public MessageProviderResponse sendMessage(MessageDTO messageDTO){
        String textToSend = messageDTO.getMessageText();
        String phoneNumber = messageDTO.getRecepientList().get(0);

        long channelId = appState.getCurrentProviderId();

        HttpResponse response = null;
        String messageId = "";
        long messageSentStatus = 0;


        if (channelId == GMSU_CHANNEL) {
            System.out.println("Trying to send a message into GMSu: " + messageDTO.getInternalMessageId());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();

            GMSuMessage.GMSuMessageBuilder gmsuMessageBuilder = GMSuMessage.builder();
            BaseProviderMessage providerMessage = gmsuMessageBuilder
                    .phoneNumber(phoneNumber)
                    .startTime(formatter.format(date))
                    .tag(messageDTO.getTag())
                    .channelOptions(
                            ChannelOptions.builder()
                                    .sms(ChannelMessage.builder().text(textToSend).ttl(300L).build())
                                    .viber(ChannelMessage.builder().text(textToSend).ttl(300L).build())
                            .build())
                    .channels(messageDTO.getChannels())
                    .build();
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                response = Unirest.post(configLoader.getGmsuConfig().getPrimaryUrl())
                        .headers(constructGmsuHeaders())
                        .body(objectMapper.writeValueAsString(providerMessage))
                        .asString();
                //TODO
                //TODO
                //TODO
                //Uncomment this!!! Now stubbed due to GMSu inaccessibility
                GMSuResponse gmsuResponse = objectMapper.readValue(response.getBody().toString(), GMSuResponse.class);
                //GMSuResponse gmsuResponse = objectMapper.readValue("{\"message_id\" :\"594e7472-68d3-4820-ac20-738cdde6d072\"}", GMSuResponse.class);
                if (gmsuResponse.getMessageId() != null) {
                    messageId = gmsuResponse.getMessageId();
                    messageSentStatus = 1;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnirestException e) {
                e.printStackTrace();
            }

        } else if (channelId == INFOBIP_CHANNEL){
            System.out.println("Trying to send a message into Infobip: " + messageDTO.getInternalMessageId());
            TextMessage smsText = messageDTO.getChannels().contains("sms") ? TextMessage.builder().text(textToSend).build() : null;
            TextMessage viberText = messageDTO.getChannels().contains("viber") ? TextMessage.builder().text(textToSend).build() : null;

            BaseProviderMessage infoBipMessage = InfoBipMessage.builder().destination(
                    Destination.builder().to(To.builder().phoneNumber(phoneNumber).build()).build())
                    .sms(smsText)
                    .viber(viberText)
                    .scenarioKey(configLoader.getInfobipConfig().getUserPassword()).build();
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                response = Unirest.post(configLoader.getInfobipConfig().getPrimaryUrl())
                        .headers(constructInfobipHeaders())
                        .body(objectMapper.writeValueAsString(infoBipMessage))
                        .asString();
                OmniResponse omniResponse = objectMapper.readValue(response.getBody().toString(), OmniResponse.class);
                long messageSentStatusGroupId = omniResponse.getMessages().get(0).getStatus().getGroupId();
                //Check if message was enqueued or delivered by the Infobip provider
                //In that case we will need to acquire its status
                if (messageSentStatusGroupId == INFOBIP_STATUS_PENDING || messageSentStatusGroupId == INFOBIP_STATUS_DELIVERED) {
                    messageId = omniResponse.getMessages().get(0).getMessageId();
                    messageSentStatus = 1;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }

        return MessageProviderResponse.builder()
                .responseCode(response == null ? -1 : response.getStatus())
                .messageId(messageId)
                .statusCode(messageSentStatus).build();
    }

    @Override
    void requestDeliveryReport(MessageDTO messageDTO) {
        HttpResponse<String> response = null;
        ObjectMapper objectMapper = new ObjectMapper();

        long channelId = messageDTO.getProviderId();
        if (channelId == GMSU_CHANNEL) {
            try {
                String reportUrl = String.format(configLoader.getGmsuConfig().getReportUrl(), messageDTO.getMessageId());
                response = Unirest.get(reportUrl).headers(constructGmsuHeaders()).asString();

                varus.messaging.service.bean.GMSu.GMSuReport.DeliveryReportResponse deliveryReportResponse = objectMapper.readValue(response.getBody(),
                        varus.messaging.service.bean.GMSu.GMSuReport.DeliveryReportResponse.class);
/*
                String mockerResponse = "{\n" +
                        "    \"time\": 1550754417000,\n" +
                        "    \"substatus\": 23,\n" +
                        "    \"status\": 2,\n" +
                        "    \"phone_number\": \"380961111111\",\n" +
                        "    \"message_id\": \"afb95ad8-a617-507f-9c72-8856a4d09e03\",\n" +
                        "    \"last_partner\": \"sms\",\n" +
                        "    \"hyber_status\": 23011,\n" +
                        "    \"extra_id\": \"AD-6640-7006\"\n" +
                        "}\n";
                varus.messaging.service.bean.GMSu.GMSuReport.DeliveryReportResponse deliveryReportResponse = objectMapper.readValue(mockerResponse,
                        varus.messaging.service.bean.GMSu.GMSuReport.DeliveryReportResponse.class);
*/


                Map<Integer, String> gmsuStatuses = new HashMap<>();
                gmsuStatuses.put(1, "у процесі доставки");
                gmsuStatuses.put(2, "успішно доставлене");
                gmsuStatuses.put(3, "помилка в обробці чи доставці ");
                Map<Integer, String> gmsuSubStatuses = new HashMap<>();

                gmsuSubStatuses.put(11, "Відправку розпочато");
                gmsuSubStatuses.put(12, "Прийнято до обробки");
                gmsuSubStatuses.put(23, "Повідомлення доставлено");
                gmsuSubStatuses.put(26, "Повідомлення не доставлено у рамках TTL");
                gmsuSubStatuses.put(36, "Помилка відправки Повідомлення");


                MessageReportRecord messageReportRecord = new MessageReportRecord(deliveryReportResponse.getMessageId(),
                        deliveryReportResponse.getPhoneNumber(),
                        new Date((long)deliveryReportResponse.getTime() * 1000),
                        deliveryReportResponse.getLastPartner(),
                        gmsuStatuses.get(deliveryReportResponse.getStatus()),
                        gmsuSubStatuses.get(deliveryReportResponse.getSubstatus()),
                        configLoader.getGmsuConfig().getProviderName(),
                        messageDTO.getInternalMessageId(),
                        "",
                        messageDTO.getMessageText()
                );

                messageReportRepository.save(messageReportRecord);
            } catch (UnirestException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (channelId == INFOBIP_CHANNEL) {

            try {
                Unirest.setTimeouts(0, 0);
                response = Unirest.get(configLoader.getInfobipConfig().getReportUrl() + "?messageId=" + messageDTO.getMessageId()
                        + "&to=" + messageDTO.getRecepientList().get(0))
                        .headers(constructInfobipHeaders())
                        .asString();
                DeliveryReportResponse deliveryReportResponse = objectMapper.readValue(response.getBody(),
                        DeliveryReportResponse.class);

                 SentSMSReport sentSMSReport = deliveryReportResponse.getResults().size() > 0 ? deliveryReportResponse.getResults().get(0) : null;
                 if (sentSMSReport != null) {
                     MessageReportRecord messageReportRecord = new MessageReportRecord(sentSMSReport.getMessageId(),
                             sentSMSReport.getTo(),
                             sentSMSReport.getSentAt(),
                             sentSMSReport.getChannel(),
                             sentSMSReport.getStatus() != null ? sentSMSReport.getStatus().getDescription() : "",
                             sentSMSReport.getError() != null ? sentSMSReport.getError().getDescription() : "",
                             configLoader.getInfobipConfig().getProviderName(),
                             messageDTO.getInternalMessageId(),
                             sentSMSReport.getPrice().getPricePerMessage().toPlainString() + sentSMSReport.getPrice().getCurrency(),
                             messageDTO.getMessageText()
                     );

                     messageReportRepository.save(messageReportRecord);
                 } else {
                     ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                     ScheduledFuture<Integer> future =
                             scheduler.schedule(new Callable<Integer>() {
                                 @Override
                                 public Integer call() throws Exception {
                                     requestDeliveryReport(messageDTO);
                                     return null;
                                 }
                             }, 60, TimeUnit.SECONDS);

                 }
            } catch (UnirestException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
