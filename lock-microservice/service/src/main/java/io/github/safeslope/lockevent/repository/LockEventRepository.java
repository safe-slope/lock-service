package io.github.safeslope.lockevent.repository;

import io.github.safeslope.entities.LockEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LockEventRepository extends JpaRepository<LockEvent, Integer> {

    // vsi eventi za  lock
    List<LockEvent> findByLock_IdOrderByEventTimeDesc(Integer lockId);
    Page<LockEvent> findByLock_IdOrderByEventTimeDesc(Integer lockId, Pageable pageable);

    // vsi eventi za ski ticket
    List<LockEvent> findBySkiTicket_IdOrderByEventTimeDesc(Integer skiTicketId);
    Page<LockEvent> findBySkiTicket_IdOrderByEventTimeDesc(Integer skiTicketId, Pageable pageable);

    // vsi eventi za ski resort
    List<LockEvent> findByLock_Locker_SkiResort_Id(Integer skiResortId);
    Page<LockEvent> findByLock_Locker_SkiResort_Id(Integer skiResortId, Pageable pageable);
}
