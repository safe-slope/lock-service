package io.github.safeslope.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "lock_event")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LockEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "event_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime eventTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lock_id")
    @JsonBackReference
    private Lock lock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ski_ticket_id")
    private SkiTicket skiTicket;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    public enum EventType {
        LOCKED,
        UNLOCKED,
        ACTION_FAILED,
        HARDWARE_FAULT,
        COMMUNICATION_ERROR,
        MODE_CHANGED,
        LOCK_DISABLED,
        LOCK_ENABLED
    }
}