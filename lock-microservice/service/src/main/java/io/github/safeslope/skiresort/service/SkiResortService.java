package io.github.safeslope.skiresort.service;

import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.skiresort.repository.SkiResortRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SkiResortService {

    private final SkiResortRepository repo;

    public SkiResortService(SkiResortRepository repo) {
        this.repo = repo;
    }

    public List<SkiResort> getAll() {
        return repo.findAll();
    }
    public Optional<SkiResort> get(Integer id) {
        return repo.findById(id);
    }

    public SkiResort create(SkiResort resort) {
        return repo.save(resort);
    }

    public Optional<SkiResort> update(Integer id, SkiResort updated) {
        return repo.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setAddress(updated.getAddress());
            existing.setLockers(updated.getLockers());
          //  existing.setSkiTickets(updated.getSkiTickets()); 
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
