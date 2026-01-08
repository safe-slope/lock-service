package io.github.safeslope.entities;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "locker")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = {"locks", "skiResort"})
public class Locker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime dateAdded;

    @Column()
    private String macAddress;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn()
    @JsonBackReference
    private SkiResort skiResort;

    @OneToMany(mappedBy = "locker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Lock> locks;
}