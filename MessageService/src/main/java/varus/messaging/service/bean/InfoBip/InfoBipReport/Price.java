package varus.messaging.service.bean.InfoBip.InfoBipReport;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Price {
    BigDecimal pricePerMessage;
    String currency;
}
