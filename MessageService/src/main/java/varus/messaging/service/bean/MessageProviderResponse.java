package varus.messaging.service.bean;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageProviderResponse {
    //HTTP response status code
    long responseCode;
    //Message senstatus code returned by a provider
    // 0 - There was an error in delivery.
    // 1 - Messages was sent correctly
    long statusCode;
    //MessgeId returned by a provider. Empty string when statusCode is 0
    String messageId;
}
