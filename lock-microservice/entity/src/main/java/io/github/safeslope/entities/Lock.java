package io.github.safeslope.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "locks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime dateAdded;

    @Column(nullable = false, unique = true)
    private String macAddress;

    @Column(nullable = false)
    private State state;

    @Column(nullable = false)
    private Mode mode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn()
    @JsonBackReference
    private Locker locker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    @JsonBackReference
    private Location location;


    public enum State {
        LOCKED,
        UNLOCKED,
        UNKNOWN
    }

    public enum Mode {
        NORMAL,
        SERVICE,
        MAINTENANCE,
        DISABLED
    }
}