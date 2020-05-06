package varus.messaging.service.dao;

import org.springframework.stereotype.Repository;
import varus.messaging.dao.bean.MessageLogRecord;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface MessageLogRepository extends CrudRepository<MessageLogRecord, Long> {
}
