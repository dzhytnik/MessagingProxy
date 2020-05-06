package varus.messaging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import varus.messaging.dao.bean.Config;
import varus.messaging.dao.bean.GSMProviderConfig;
import varus.messaging.service.async.RabbitConsumer;
import varus.messaging.service.dao.ConfigRepository;
import varus.messaging.service.dao.MessageLogRepository;
import varus.messaging.service.dao.ProviderRepository;

@Component
public class ConfigLoader {
    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private MessageLogRepository messageLogRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    RabbitConsumer jmsConsumer;


    private static Config config;
    private static GSMProviderConfig infobipConfig;
    private static GSMProviderConfig gmsuConfig;


    @Scheduled(fixedRate = 5000)
    public void loadConfiguration() {
        config = configRepository.findById(1l).get();
        infobipConfig = providerRepository.findByProviderName("Infobip");
        gmsuConfig = providerRepository.findByProviderName("gmsu");
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public GSMProviderConfig getInfobipConfig() {
        return infobipConfig;
    }

    public void setInfobipConfig(GSMProviderConfig infobipConfig) {
        this.infobipConfig = infobipConfig;
    }

    public GSMProviderConfig getGmsuConfig() {
        return gmsuConfig;
    }

    public void setGmsuConfig(GSMProviderConfig gmsuConfig) {
        this.gmsuConfig = gmsuConfig;
    }
}
