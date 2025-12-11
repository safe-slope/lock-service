package io.github.safeslope.lockevent.repository;

import io.github.safeslope.entities.LockEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LockEventRepository extends JpaRepository<LockEvent, Integer> {

    // vsi eventi za  lock
    List<LockEvent> findByLockId_IdOrderByEventTimeDesc(Integer lockId);

    // vsi eventi za ski ticket
    List<LockEvent> findBySkiTicket_IdOrderByEventTimeDesc(Integer skiTicketId);
}
