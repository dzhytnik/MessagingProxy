package varus.messaging.service.async;

import org.springframework.beans.factory.annotation.Autowired;
import varus.messaging.service.ConfigLoader;
import varus.messaging.service.bean.MessageDTO;
import varus.messaging.service.bean.MessageProviderResponse;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseMessageSender {
    @Autowired
    ConfigLoader configLoader;

    protected Map<String, String> constructInfobipHeaders() {
        Map resultMap = new HashMap();
        resultMap.put("Content-Type", "application/json");
        resultMap.put("Authorization", "App c7bb509341ec624a0a41de7754356208-7d2e4651-6117-4a75-be96-2ceb5683fc72");
        return resultMap;
    }

    protected Map<String, String> constructGmsuHeaders() {
        Map resultMap = new HashMap();

        String username = configLoader.getGmsuConfig().getUsername();
        String password = configLoader.getGmsuConfig().getUserPassword();
        String authorization = username + ":" + password;
        String encodedBytes = Base64.getEncoder().encodeToString(authorization.getBytes());
        authorization = "Basic " + encodedBytes;

        resultMap.put("Content-Type", "application/json");
        resultMap.put("Authorization", authorization);
        return resultMap;
    }

    abstract MessageProviderResponse sendMessage(MessageDTO messageDTO);
    abstract void requestDeliveryReport(MessageDTO messageDTO);

}