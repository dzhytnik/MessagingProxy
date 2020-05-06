package varus.messaging.service.bean.InfoBip;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class OmniResponseStatus {
    @JsonProperty(required = false)
    int groupId;
    @JsonProperty(required = false)
    String groupName;
    @JsonProperty(required = false)
    int id;
    @JsonProperty(required = false)
    String name;
    @JsonProperty(required = false)
    String description;
    @JsonProperty(required = false)
    String action;
}
