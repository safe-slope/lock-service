package io.github.safeslope.lock.service;

import io.github.safeslope.entities.Lock;
import io.github.safeslope.lock.repository.LockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LockService{
    private final LockRepository repo;

    public LockService(LockRepository repo) {
        this.repo = repo;
    }

    public List<Lock> getAllLocks() {
        return repo.findAll();
    }

    public Lock getLock(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new LockNotFoundException(id));
     
    }

    public Lock getByMacAddress(String mac) {
        return repo.findByMacAddress(mac);
             .orElseThrow(() -> new LockNotFoundException(mac));
    }

    public Lock create(Lock lock) {
        return repo.save(lock);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
