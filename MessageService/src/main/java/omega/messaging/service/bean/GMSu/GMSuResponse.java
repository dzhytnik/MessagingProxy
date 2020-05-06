package varus.messaging.service.bean.GMSu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GMSuResponse {
    @JsonProperty(value = "message_id", required = false)
    String messageId;
    @JsonProperty(value = "error_code", required = false)
    long errorCode;
    @JsonProperty(value = "error_text", required = false)
    String errorText;
}
