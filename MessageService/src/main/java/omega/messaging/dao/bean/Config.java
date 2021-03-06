package varus.messaging.dao.bean;

import javax.persistence.*;

@Entity
@Table(name="global_config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="number_of_attempts")
    private int numberOfAttempts;
    @Column(name="secondary_channel_timeslot")
    private int secondaryChannelTimeslot;
    @Column(name="default_provider_id")
    private Long defaultProviderId;
    @Column(name="ad_time_window_start")
    private String adTimeWindowStart;
    @Column(name="ad_time_window_end")
    private String adTimeWindowEnd;
    @Column(name="auth_user_name")
    private String authUserName;
    @Column(name="auth_password")
    private String authPassword;



    public int getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public int getSecondaryChannelTimeslot() {
        return secondaryChannelTimeslot;
    }

    public Long getDefaultProviderId() {
        return defaultProviderId;
    }

    public String getAdTimeWindowStart() {
        return adTimeWindowStart;
    }

    public String getAdTimeWindowEnd() {
        return adTimeWindowEnd;
    }

    public String getAuthUserName() {
        return authUserName;
    }

    public String getAuthPassword() {
        return authPassword;
    }
}
