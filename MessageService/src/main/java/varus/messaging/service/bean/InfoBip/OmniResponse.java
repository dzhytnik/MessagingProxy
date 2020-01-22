package varus.messaging.service.bean.InfoBip;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
public class OmniResponse {
    @JsonProperty(required = false)
    String bulkId;
    List<OmniResponseDetails> messages;
}
