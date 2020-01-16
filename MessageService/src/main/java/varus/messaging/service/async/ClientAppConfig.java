package varus.messaging.service.async;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = {"varus.messaging.dao.bean"})
public class ClientAppConfig {
}
