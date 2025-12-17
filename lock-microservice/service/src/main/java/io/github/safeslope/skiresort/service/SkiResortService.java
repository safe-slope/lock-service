package io.github.safeslope.skiresort.service;

import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.lockevent.service.LockEventNotFoundException;
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
    public SkiResort get(Integer id) {

        return repo.findById(id).
                orElseThrow(() -> new SkiResortNotFoundException(id));
    }

    public SkiResort create(SkiResort resort) {
        return repo.save(resort);
    }

    public SkiResort update(Integer id, SkiResort updated) {
        // FIXME to rabi najprej najdt taprav entity in ga spremenit
        return repo.save(updated);
    }

    public boolean delete(Integer id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }
}
