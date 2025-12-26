package io.github.safeslope.lockevent.service;

import io.github.safeslope.entities.LockEvent;
import io.github.safeslope.lock.repository.LockRepository;
import io.github.safeslope.lock.service.LockNotFoundException;
import io.github.safeslope.lockevent.repository.LockEventRepository;
import io.github.safeslope.skiresort.repository.SkiResortRepository;
import io.github.safeslope.skiresort.service.SkiResortNotFoundException;
import io.github.safeslope.skiticket.repository.SkiTicketRepository;
import io.github.safeslope.skiticket.service.SkiTicketNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LockEventService {

    private final LockEventRepository lockEventRepository;
    private final SkiResortRepository skiResortRepository;
    private final SkiTicketRepository skiTicketRepository;
    private final LockRepository lockRepository;

    public LockEventService(LockEventRepository lockEventRepository, SkiResortRepository skiResortRepository, SkiTicketRepository skiTicketRepository, LockRepository lockRepository) {
        this.lockEventRepository = lockEventRepository;
        this.skiResortRepository = skiResortRepository;
        this.skiTicketRepository = skiTicketRepository;
        this.lockRepository = lockRepository;
    }

    public List<LockEvent> getAll() {
        return lockEventRepository.findAll();
    }

    public LockEvent get(Integer id) {
        return lockEventRepository.findById(id)
                .orElseThrow(() -> new LockEventNotFoundException(id));
    }

    public List<LockEvent> getAllByLock(Integer lockId) {
        if(!lockRepository.existsById(lockId)){
            throw new LockNotFoundException(lockId);
        }
        return lockEventRepository.findByLock_IdOrderByEventTimeDesc(lockId);
    }

    public List<LockEvent> getAllBySkiTicket(Integer skiTicketId) {
        if (!skiTicketRepository.existsById(skiTicketId)) {
            throw new SkiTicketNotFoundException(skiTicketId);
        }
        return lockEventRepository.findBySkiTicket_IdOrderByEventTimeDesc(skiTicketId);
    }

    public List<LockEvent> getAllBySkiResort(Integer skiResortId) {
        if (!skiResortRepository.existsById(skiResortId)) {
            throw new SkiResortNotFoundException(skiResortId);
        }
        return lockEventRepository.findByLock_Locker_SkiResort_Id(skiResortId);
    }

    public LockEvent create(LockEvent event) {
        if (event.getEventTime() == null) {
            event.setEventTime(LocalDateTime.now());
        }
        return lockEventRepository.save(event);
    }

}
