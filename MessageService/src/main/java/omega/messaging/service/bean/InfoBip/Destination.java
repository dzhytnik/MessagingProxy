package varus.messaging.service.bean.InfoBip;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class Destination {
    To to;
}
