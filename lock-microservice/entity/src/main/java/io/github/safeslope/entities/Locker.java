package io.github.safeslope.entities;

import java.sql.Timestamp;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "locker")
@NamedQueries(value = 
    {
        //tu vpiši uporabne querije za locker
        @NamedQuery(name = "Locker.getAll", query = "SELECT l FROM Locker l"),
        @NamedQuery(name = "Locker.findByMacAddress", query = "SELECT l FROM Locker l WHERE l.macAddress = :macAddress")
    }
)
public class Locker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_added", columnDefinition = "TIMESTAMP")
    private Timestamp dateAdded;

    @Column(name = "mac_address")
    private String macAddress;

    @ManyToOne
    @JoinColumn(name = "ski_resort_id")
    private SkiResort skiResort;

    @OneToMany(mappedBy = "locker", cascade = CascadeType.ALL)
    private List<Lock> locks;

    //getters and setters
    public Integer getId() {
        return id;
    }

    public Timestamp getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Timestamp dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public SkiResort getSkiResort() {
        return skiResort;
    }

    public void setSkiResort(SkiResort skiResort) {
        this.skiResort = skiResort;
    }

    public List<Lock> getLocks() {
        return locks;
    }

    public void setLocks(List<Lock> locks) {
        this.locks = locks;
    }
}
