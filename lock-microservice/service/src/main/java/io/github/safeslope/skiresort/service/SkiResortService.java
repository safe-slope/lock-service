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
    
    
     public SkiResort get(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new SkiResortNotFoundException(id));
    }

    public SkiResort create(SkiResort resort) {
        return repo.save(resort);
    }

     public SkiResort update(Integer id, SkiResort updated) {
        SkiResort existing = repo.findById(id)
            .orElseThrow(() -> new SkiResortNotFoundException(id));

        existing.setName(updated.getName());
        existing.setAddress(updated.getAddress());
        return repo.save(existing);
    }

    public void delete(Integer id) {
        if (!repo.existsById(id)) {
            throw new SkiResortNotFoundException(id);
        }
        repo.deleteById(id);
    }
}
