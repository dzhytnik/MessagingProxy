package varus.messaging.service.bean.GMSu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChannelMessage {
    String text;
    @JsonProperty("alpha_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String alphaName;
    Long ttl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String img;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String caption;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String action;
}
