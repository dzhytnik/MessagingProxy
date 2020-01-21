package varus.messaging.service.dao;

import org.springframework.data.jpa.repository.Query;
import varus.messaging.dao.bean.GSMProviderConfig;
import org.springframework.data.repository.CrudRepository;

public interface ProviderRepository extends CrudRepository<GSMProviderConfig, Long> {
    GSMProviderConfig findByProviderName(String name);
}
