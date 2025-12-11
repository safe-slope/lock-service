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

    public Optional<SkiTicket> get(Integer id) {
        return repo.findById(id);
    }

    public SkiTicket create(SkiTicket ticket) {
        return repo.save(ticket);
    }

    public Optional<SkiTicket> update(Integer id, SkiTicket updated) {
        return repo.findById(id).map(existing -> {

            existing.setValidFrom(updated.getValidFrom());
            existing.setValidTo(updated.getValidTo());
            existing.setSkiResort(updated.getSkiResort());

            return repo.save(existing);
        });
    }

    public boolean delete(Integer id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }
}
