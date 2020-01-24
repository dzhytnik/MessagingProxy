package varus.messaging.service.bean.InfoBip.InfoBipReport;

import lombok.Data;

import java.util.Date;

@Data
public class SentSMSReport {
    String bulkId;
    String messageId;
    String to;
    String channel;
    Date sentAt;
    Date doneAt;
    int messageCount;
    String mccMnc;
    Price price;
    Status status;
    Error error;
}
