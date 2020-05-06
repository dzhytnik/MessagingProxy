package varus.messaging.dao.bean;

import javax.persistence.*;

@Entity
@Table(name="gsm_provider")
public class GSMProviderConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="provider_name")
    private String providerName;

    @Column(name="primary_url")
    private String primaryUrl;

    @Column(name="secondary_url")
    private String secondaryUrl;

    @Column(name="report_url")
    private String reportUrl;

    private String username;

    @Column(name="user_password")
    private String userPassword;

    public String getProviderName() {
        return providerName;
    }

    public String getPrimaryUrl() {
        return primaryUrl;
    }

    public String getSecondaryUrl() {
        return secondaryUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getReportUrl() {
        return reportUrl;
    }
}
