package io.github.safeslope.entities;

import java.util.List;
import jakarta.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ski_resort")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = {"lockers", "skiTickets"})
public class SkiResort {
    @Id
    //without generation type, IDs are set externally
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "skiResort", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Locker> lockers;

    @OneToMany(mappedBy = "skiResort", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SkiTicket> skiTickets;
}