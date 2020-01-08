package varus.messaging.service;

import org.springframework.data.repository.CrudRepository;
import varus.messaging.dao.bean.GSMProviderConfig;

public interface ProviderRepository extends CrudRepository<GSMProviderConfig, Long> {
    GSMProviderConfig findByProviderName(String name);

}
