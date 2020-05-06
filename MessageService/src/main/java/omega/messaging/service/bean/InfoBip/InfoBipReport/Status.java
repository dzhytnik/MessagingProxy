package varus.messaging.service.bean.InfoBip.InfoBipReport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Status {
    int groupId;
    String groupName;
    int id;
    String name;
    String description;
    String action;
}
