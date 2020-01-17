package varus.messaging.service.bean.InfoBip;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class InfoBipMessage {
    String scenarioKey;
    @Singular
    List<Destination> destinations;
    TextMessage sms;
    TextMessage viber;
}
