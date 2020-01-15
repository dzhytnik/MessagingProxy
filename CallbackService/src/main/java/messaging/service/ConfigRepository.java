package messaging.service;

import org.springframework.data.repository.CrudRepository;
import messaging.dao.bean.Config;

public interface ConfigRepository extends CrudRepository<Config, Long> {

}
