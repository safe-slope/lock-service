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
@Table(name = "lock_event")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"lockId"})
public class LockEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "event_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime eventTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lock_id")
    @JsonBackReference
    private Lock lockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ski_ticket_id")
    private SkiTicket skiTicket;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private enum EventType {
        LOCKED,
        UNLOCKED,
        MALFUNCTION
    }
}