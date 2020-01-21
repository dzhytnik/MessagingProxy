package varus.messaging.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import varus.messaging.service.dao.ConfigRepository;
import varus.messaging.service.dao.ProviderRepository;

import java.util.Date;

@Component
public class MessagingServiceAppState implements InitializingBean {
    @Autowired
    ConfigRepository configRepository;

    Long currentProviderId;

    Long currentProviderTryCount = 0L;

    Date reserveProviderStartTime;

    public Long getCurrentProviderId() {
        return currentProviderId;
    }

    public void setCurrentProviderId(Long currentProviderId) {
        this.currentProviderId = currentProviderId;
    }

    public Long getCurrentProviderTryCount() {
        return currentProviderTryCount;
    }

    public void setCurrentProviderTryCount(Long currentProviderTryCount) {
        this.currentProviderTryCount = currentProviderTryCount;
    }

    public Date getReserveProviderStartTime() {
        return reserveProviderStartTime;
    }

    public void setReserveProviderStartTime(Date reserveProviderStartTime) {
        this.reserveProviderStartTime = reserveProviderStartTime;
    }

    public void incrCurrentProviderTryCount() {
        this.currentProviderTryCount++;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        currentProviderId = configRepository.findById(1L).get().getDefaultProviderId();
    }
}
