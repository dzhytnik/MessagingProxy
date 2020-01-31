package varus.messaging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import varus.messaging.service.dao.ConfigRepository;

@Service
public class ApplicationPropertyService {
    @Autowired
    ConfigRepository configRepository;

    public String getTimeWindowStart(){
        return configRepository.findById(1l).get().getAdTimeWindowStart();
    }

    public String getTimeWindowEnd(){
        return configRepository.findById(1l).get().getAdTimeWindowEnd();
    }

    public String getAuthUserName() {
        return  configRepository.findById(1l).get().getAuthUserName();
    }

    public String getAuthPassword() {
        return  configRepository.findById(1l).get().getAuthPassword();
    }
}
