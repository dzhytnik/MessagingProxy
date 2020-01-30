package varus.messaging.dao.bean;

import javax.persistence.*;
import java.util.Date;

@Entity(name="message_report")
public class MessageReportRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="message_id")
    String messageId;
    @Column(name="phone_number")
    String phoneNumber;
    @Column(name="sent_at")
    Date sentAt;
    String channel;
    String status;
    @Column(name="error_str")
    String errorStr;
    @Column(name="sent_by_provider")
    String sentBy;
    @Column(name="internal_message_id")
    String internalMessageId;
    String price;
    @Column(name="msg_text")
    String msgText;

    public MessageReportRecord(String messageId, String phoneNumber, Date sentAt, String channel,
                               String status, String errorStr, String sentBy, String internalMessageId, String price, String msgText) {
        this.messageId = messageId;
        this.phoneNumber = phoneNumber;
        this.sentAt = sentAt;
        this.channel = channel;
        this.status = status;
        this.errorStr = errorStr;
        this.sentBy = sentBy;
        this.internalMessageId = internalMessageId;
        this.price = price;
        this.msgText = msgText;
    }


}
