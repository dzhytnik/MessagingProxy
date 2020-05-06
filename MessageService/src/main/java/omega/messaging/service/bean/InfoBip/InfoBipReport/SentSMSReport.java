package varus.messaging.service.bean.InfoBip.InfoBipReport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SentSMSReport {
    String text;
    String messageId;
    String to;
    String channel;
    Date  sentAt;
    Date doneAt;
    int messageCount;
    String mccMnc;
    String from;
    Price price;
    Status status;
    @JsonProperty(required = false)
    Error error;
}
