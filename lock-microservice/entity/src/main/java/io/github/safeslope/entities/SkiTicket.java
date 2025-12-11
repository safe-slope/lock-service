package io.github.safeslope.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "ski_ticket")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"skiResort"})
public class SkiTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "valid_from", columnDefinition = "TIMESTAMP")
    private LocalDateTime validFrom;

    @Column(name = "valid_to", columnDefinition = "TIMESTAMP")
    private LocalDateTime validTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ski_resort_id")
    @JsonBackReference
    private SkiResort skiResort;
}