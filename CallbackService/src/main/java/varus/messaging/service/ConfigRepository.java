package varus.messaging.service;

import org.springframework.data.repository.CrudRepository;
import varus.messaging.dao.bean.Config;

public interface ConfigRepository extends CrudRepository<Config, Long> {

}
