package io.github.safeslope.entities;

import javax.persistence.*;

@Entity
@Table(name = "event_type")
@NamedQueries(value = 
    {
        //tu vpiši uporabne querije
        @NamedQuery(name = "EventType.getAll", query = "SELECT e FROM EventType e")
    }
)

public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING) //shrani se string
    private EventTypeName name;

    private enum EventTypeName {
        LOCK,
        UNLOCK,
        MALFUNCTION
    }

    @OneToMany(mappedBy = "event_type", cascade = CascadeType.ALL)
    private List<LockEvent> lockEvents;

    //getters and setters
    public Integer getId() {
        return id;
    }

    public EventTypeName getName() {
        return name;
    }

    public void setName(EventTypeName name) {
        this.name = name;
    }

    public List<Lock> getLocks() {
        return locks;
    }
}