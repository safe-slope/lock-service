package io.github.safeslope.skiresort.service;

import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.Locker;
import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.skiresort.repository.SkiResortRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public SkiResort getByName(String name) {
        // FIXME potrebna je implementacija izjeme (kot pri get)
        return repo.findByName(name);
    }

    public List<Locker> getLockers(Integer id) {
        // TODO implementiraj metodo, ki najde vse Lockerje za SkiResort s podanim id-jem
    }

    public List<Lock> getLocks(Integer id) {
        // TODO implementiraj metodo, ki najde vse Locke za SkiResort s podanim id-jem
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
