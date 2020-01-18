package varus.messaging.service.bean.GMSu;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChannelOptions {
    ChannelMessage sms;
    ChannelMessage viber;
}
