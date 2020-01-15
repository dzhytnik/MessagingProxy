package messaging.dao.bean;

import javax.persistence.*;
import java.util.Date;

@Entity(name="message_log")
public class MessageLogRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="recipient_list")
    String recipientList;
    @Column(name="msg_text")
    String message;
    @Column(name="date_stamp")
    Date date;
    @Column(name="status")
    int sendingStatus;
    @Column(name="message_id")
    String messageId;

    public MessageLogRecord(String recipientList, String message, Date date, int sendingStatus, String messageId) {
        this.recipientList = recipientList;
        this.message = message;
        this.date = date;
        this.sendingStatus = sendingStatus;
        this.messageId = messageId;
    }
}
