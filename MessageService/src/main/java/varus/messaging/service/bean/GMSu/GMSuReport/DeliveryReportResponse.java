package varus.messaging.service.bean.GMSu.GMSuReport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeliveryReportResponse {
    long time;
    int substatus;
    int status;
    @JsonProperty("phone_number")
    String phoneNumber;
    @JsonProperty("last_partner")
    String lastPartner;
    @JsonProperty("message_id")
    String messageId;
    @JsonProperty("hyber_status")
    String hyberStatus;
    @JsonProperty("extra_id")
    String extraId;
}
