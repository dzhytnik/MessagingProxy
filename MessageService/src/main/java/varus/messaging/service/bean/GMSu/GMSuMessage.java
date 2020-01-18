package varus.messaging.service.bean.GMSu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import varus.messaging.service.bean.common.BaseProviderMessage;

import java.util.List;

@Data
@Builder
public class GMSuMessage implements BaseProviderMessage {
    @JsonProperty("phone_number")
    String phoneNumber;
    @JsonProperty("start_time")
    String startTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String tag;
    List<String> channels;
    @JsonProperty("channel_options")
    ChannelOptions channelOptions;
}
