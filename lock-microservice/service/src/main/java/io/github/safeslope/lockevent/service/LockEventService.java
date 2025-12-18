package io.github.safeslope.lockevent.service;

import io.github.safeslope.entities.LockEvent;
import io.github.safeslope.lockevent.repository.LockEventRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LockEventService {

    private final LockEventRepository repo;

    public LockEventService(LockEventRepository repo) {
        this.repo = repo;
    }

    public List<LockEvent> getAll() {
        return repo.findAll();
    }

    public LockEvent get(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new LockEventNotFoundException(id));
    }

    public List<LockEvent> getAllByLock(Integer lockId) {
        return repo.findByLock_IdOrderByEventTimeDesc(lockId);
    }

    public List<LockEvent> getAllBySkiTicket(Integer skiTicketId) {
        return repo.findBySkiTicket_IdOrderByEventTimeDesc(skiTicketId);
    }

    public List<LockEvent> getAllBySkiResort(Integer skiResortId) {
        return repo.findBySkiResort_Id(skiResortId);
    }

    public LockEvent create(LockEvent event) {
        if (event.getEventTime() == null) {
            event.setEventTime(LocalDateTime.now());
        }
        return repo.save(event);
    }

}
