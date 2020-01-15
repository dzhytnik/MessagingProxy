package messaging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import messaging.dao.bean.Config;
import messaging.dao.bean.GSMProviderConfig;

@Component
public class ConfigLoader {
    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ProviderRepository providerRepository;


    private static Config config;
    private static GSMProviderConfig infobipConfig;
    private static GSMProviderConfig gmsuConfig;
    private static ConfigLoader instance = null;


    @Scheduled(fixedRate = 2_000)
    public void loadConfiguration() {
        config = configRepository.findById(1l).get();
        infobipConfig = providerRepository.findByProviderName("Infobip");
        gmsuConfig = providerRepository.findByProviderName("gmsu");
    }

    public static ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
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
