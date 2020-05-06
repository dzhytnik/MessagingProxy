package varus.messaging.service.dao;

import org.springframework.data.repository.CrudRepository;
import varus.messaging.dao.bean.ClientConfig;

public interface ClientConfigRepository extends CrudRepository<ClientConfig, Long> {
}
