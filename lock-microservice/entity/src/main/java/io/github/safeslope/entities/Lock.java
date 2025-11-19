package io.github.safeslope.entities;

import java.lang.annotation.Inherited;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "lock")
@NamedQueries(value = 
    {
        //tu vpiši uporabne querije za lock
        @NamedQuery(name = "Lock.getAll", query = "SELECT l FROM Lock l"),
        @NamedQuery(name = "Lock.findByMacAddress", query = "SELECT l FROM Lock l WHERE l.macAddress = :macAddress"),
        @NamedQuery(name = "Lock.findByLockerId", query = "SELECT l FROM Lock l WHERE l.locker.id = :lockerId")
    }
)

public class Lock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_added", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAdded;

    @Column(name = "mac_address")
    private String macAddress;

    @ManyToOne
    @JoinColumn(name = "locker_id")
    private Locker locker;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    //getters and setters
    public Integer getId() {
        return id;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Locker getLocker() {
        return locker;
    }

    public void setLocker(Locker locker) {
        this.locker = locker;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
