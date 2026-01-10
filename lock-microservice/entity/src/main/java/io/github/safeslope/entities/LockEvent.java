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
    @JoinColumn(name = "ski_ticket_id", nullable = true)
    private SkiTicket skiTicket;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    public enum EventType {
        LOCK,
        UNLOCK,
        ACTION_FAILED,
        COMMUNICATION_ERROR,
        SET_MODE_TO_NORMAL,
        SET_MODE_TO_SERVICE,
        SET_MODE_TO_MAINTENANCE,
        SET_MODE_TO_DISABLED,
    }
}