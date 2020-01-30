package varus.messaging.service.bean.InfoBip;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import varus.messaging.service.bean.common.BaseProviderMessage;

import java.util.List;

@Data
@Builder
public class InfoBipMessage implements BaseProviderMessage {
    String scenarioKey;
    @Singular
    List<Destination> destinations;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    TextMessage sms;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    TextMessage viber;
}
