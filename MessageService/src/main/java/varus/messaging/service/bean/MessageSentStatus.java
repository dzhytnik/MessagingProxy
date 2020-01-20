package varus.messaging.service.bean;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageSentStatus {
    int sentStatus;
    String messageId;
}
