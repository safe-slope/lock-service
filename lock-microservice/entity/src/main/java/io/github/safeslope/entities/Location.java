package io.github.safeslope.entities;

import javax.persistence.*;

@Entity
@Table(name = "location")
@NamedQueries(value = 
    {
        //tu vpiši uporabne querije
        @NamedQuery(name = "Location.getAll", query = "SELECT l FROM Location l")
    }
)

public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Integer id;

    @Column(name = "coordinates", columnDefinition = "Point")
    private Point coordinates;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Lock> locks;

    //getters and setters
    public Integer getId() {
        return id;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public List<Lock> getLocks() {
        return locks;
    }

    public void setLocks(List<Lock> locks) {
        this.locks = locks;
    }
}
