package varus.messaging.service.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import varus.messaging.dao.bean.MessageReportRecord;

@Repository
public interface MessageReportRepository extends CrudRepository<MessageReportRecord, Long> {
}
