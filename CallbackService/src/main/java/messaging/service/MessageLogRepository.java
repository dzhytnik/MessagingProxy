package messaging.service;

import org.springframework.data.repository.CrudRepository;
import messaging.dao.bean.MessageLogRecord;

public interface MessageLogRepository extends CrudRepository<MessageLogRecord, Long> {
}
