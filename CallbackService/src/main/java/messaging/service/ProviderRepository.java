package messaging.service;

import org.springframework.data.repository.CrudRepository;
import messaging.dao.bean.GSMProviderConfig;

public interface ProviderRepository extends CrudRepository<GSMProviderConfig, Long> {
    GSMProviderConfig findByProviderName(String name);

}
