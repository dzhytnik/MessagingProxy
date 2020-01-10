package varus.messaging.service;

import org.springframework.data.repository.CrudRepository;
import varus.messaging.dao.bean.MessageLogRecord;

public interface MessageLogRepository extends CrudRepository<MessageLogRecord, Long> {
}
