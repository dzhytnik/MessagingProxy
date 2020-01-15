package messaging.dao.bean;

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

    public int getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public int getSecondaryChannelTimeslot() {
        return secondaryChannelTimeslot;
    }
}
