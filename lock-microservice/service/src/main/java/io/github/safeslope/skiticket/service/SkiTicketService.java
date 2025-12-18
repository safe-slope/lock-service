package io.github.safeslope.skiticket.service;

import io.github.safeslope.entities.SkiTicket;
import io.github.safeslope.skiticket.repository.SkiTicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SkiTicketService {

    private final SkiTicketRepository repo;

    public SkiTicketService(SkiTicketRepository repo) {
        this.repo = repo;
    }

    public List<SkiTicket> getAll() {
        return repo.findAll();
    }

    public SkiTicket get(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new SkiTicketNotFoundException(id));
    }

    public SkiTicket create(SkiTicket ticket) {
        return repo.save(ticket);
    }

    public SkiTicket update(Integer id, SkiTicket updated) {
        SkiTicket existing = repo.findById(id)
            .orElseThrow(() -> new SkiTicketNotFoundException(id));
        existing.setValidFrom(updated.getValidFrom());
        existing.setValidTo(updated.getValidTo());
        return repo.save(existing);
    }

    public void delete(Integer id) {
        if (!repo.existsById(id)) {
            throw new SkiTicketNotFoundException(id);
        }
        repo.deleteById(id);
    }

    public List<SkiTicket> getAllBySkiResortId(Integer skiResortId) {
        return repo.findBySkiResort_Id(skiResortId);
    }
}
