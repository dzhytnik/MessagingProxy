package varus.messaging.service.bean.InfoBip;

import lombok.Builder;
import lombok.Data;

@Data
public class OmniResponseDetails {
    ResponseTo to;
    OmniResponseStatus status;
    String messageId;
}
