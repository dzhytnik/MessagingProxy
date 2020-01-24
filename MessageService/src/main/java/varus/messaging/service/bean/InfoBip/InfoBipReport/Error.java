package varus.messaging.service.bean.InfoBip.InfoBipReport;

import lombok.Data;

@Data
public class Error {
    int groupId;
    String groupName;
    int id;
    String name;
    String description;
    String permanent;
}
