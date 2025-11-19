package io.github.safeslope.entities;

import javax.persistence.*;

@Entity
@Table(name = "ski_resort")
@NamedQueries(value = 
    {
        //tu vpiši uporabne querije
        @NamedQuery(name = "SkiResort.getAll", query = "SELECT sr FROM SkiResort sr")
    }
)
public class SkiResort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "ski_resort", cascade = CascadeType.ALL)
    private List<Locker> lockers;

    @OneToMany(mappedBy = "ski_resort", cascade = CascadeType.ALL)
    private List<SkiTicket> skiTickets;

    //getters and setters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Locker> getLockers() {
        return lockers;
    }

    public void setLockers(List<Locker> lockers) {
        this.lockers = lockers;
    }

    public List<SkiTicket> getSkiTickets() {
        return skiTickets;
    }

    public void setSkiTickets(List<SkiTicket> skiTickets) {
        this.skiTickets = skiTickets;
    }
}
