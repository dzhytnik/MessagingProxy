package varus.messaging.dao.bean;


import javax.persistence.*;

@Entity(name="client_config")
public class ClientConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="client_name")
    String name;
    @Column(name="number_of_attempts")
    Integer numberOfAttempts;
    Integer priority;
    @Column(name="failover_sms_allowed")
    Boolean failoverSmsAllowed;
    @Column(name="time_window_restricted")
    Boolean timeWindowRestricted;

    public String getName() {
        return name;
    }

    public Integer getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public Integer getPriority() {
        return priority;
    }

    public Boolean getFailoverSmsAllowed() {
        return failoverSmsAllowed;
    }

    public Boolean getTimeWindowRestricted() {
        return timeWindowRestricted;
    }
}
