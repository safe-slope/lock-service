package io.github.safeslope.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "ski_ticket")
@NamedQueries(value = 
    {
        //tu vpiši uporabne querije za ski ticket
        @NamedQuery(name = "SkiTicket.getAll", query = "SELECT st FROM SkiTicket st")
    }
)
public class SkiTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "valid_from", columnDefinition = "TIMESTAMP")
    private LocalDateTime validFrom;

    @Column(name = "valid_to", columnDefinition = "TIMESTAMP")
    private LocalDateTime validTo;

    @ManyToOne
    @JoinColumn(name = "ski_resort_id")
    private SkiResort skiResort;

    //getters and setters
    public Integer getId() {
        return id;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    public SkiResort getSkiResort() {
        return skiResort;
    }

    public void setSkiResort(SkiResort skiResort) {
        this.skiResort = skiResort;
    }
}
