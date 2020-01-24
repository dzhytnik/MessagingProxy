package varus.messaging.service.bean.InfoBip.InfoBipReport;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class DeliveryReportResponse {
    List<SentSMSReport> results;
}
