package io.github.safeslope.lock.service;

import io.github.safeslope.entities.Lock;
import io.github.safeslope.lock.repository.LockRepository;
import io.github.safeslope.locker.service.LockerNotFoundException;
import io.github.safeslope.skiresort.service.SkiResortNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class LockService {
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
        return repo.findByMacAddress(mac)
            .orElseThrow(() -> new LockNotFoundException(mac));
    }

    public Lock create(Lock lock) {
        return repo.save(lock);
    }

    public Lock update(Integer id, Lock lock) {
        repo.findById(id).orElseThrow(() -> new LockNotFoundException(id));
        lock.setId(id);
        return repo.save(lock);
    }

    public void delete(Integer id) {
        if (!repo.existsById(id)) {
            throw new LockNotFoundException(id);
        }
        repo.deleteById(id);
    }

    public List<Lock> getAllByLockerId(Integer lockerId) {
        if (!repo.existsById(lockerId)) {
            throw new LockerNotFoundException(lockerId);
        }
        return repo.findByLocker_Id(lockerId);
    }

    public List<Lock> getAllBySkiResortId(Integer skiResortId){
        if (!repo.existsById(skiResortId)) {
            throw new SkiResortNotFoundException(skiResortId);
        }
        return repo.findByLocker_SkiResort_Id(skiResortId);
    }
}