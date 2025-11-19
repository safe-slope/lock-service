package io.github.safeslope.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "lock_event")
@NamedQueries(value = 
    {
        //tu vpiši uporabne querije za lock event
        @NamedQuery(name = "LockEvent.getAll", query = "SELECT le FROM LockEvent le")
    }
)

public class LockEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "event_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime eventTime;

    @ManyToOne
    @JoinColumn(name = "lock_id")
    private Lock lockId;

    @ManyToOne
    @JoinColumn(name = "ski_ticket_id")
    private SkiTicket skiTicket;

    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    //getters and setters
    public Integer getId(){
        return this.id;
    }

    public LocalDateTime getEventTime(){
        return this.eventTime;
    }

    public void setEventTime(LocalDateTime eventTime){
        this.eventTime = eventTime;
    }

    public Lock getLockId(){
        return this.lockId;
    }

    public void setLockId(Lock lockId){
        this.lockId = lockId;
    }

    public SkiTicket getSkiTicket(){
        return this.skiTicket;
    }

    public void setSkiTicket(SkiTicket skiTicket){
        this.skiTicket = skiTicket;
    }

    public EventType getEventType(){
        return this.eventType;
    }

    public void setEventType(EventType eventType){
        this.eventType = eventType;
    }
}
