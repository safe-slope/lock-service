package io.github.safeslope.lock.service;

import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.lock.repository.LockRepository;
import io.github.safeslope.locker.repository.LockerRepository;
import io.github.safeslope.locker.service.LockerNotFoundException;
import io.github.safeslope.skiresort.repository.SkiResortRepository;
import io.github.safeslope.skiresort.service.SkiResortNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class LockService {
    private final LockRepository lockRepository;
    private final LockerRepository lockerRepository;
    private final SkiResortRepository skiResortRepository;

    public LockService(LockRepository lockRepository, LockerRepository lockerRepository, SkiResortRepository skiResortRepository) {
        this.lockRepository = lockRepository;
        this.lockerRepository = lockerRepository;
        this.skiResortRepository = skiResortRepository;
    }

    public List<Lock> getAllLocks() {
        return lockRepository.findAll();
    }

    public Lock getLock(Integer id) {
        return lockRepository.findById(id)
            .orElseThrow(() -> new LockNotFoundException(id));
    }

    public Lock getByMacAddress(String mac) {
        Lock lock = lockRepository.findByMacAddress(mac);

        if(lock != null){
            return lock;
        }
        else{
            throw new LockNotFoundException(mac);
        }
    }

    public Lock create(Lock lock) {
        return lockRepository.save(lock);
    }

    public Lock update(Integer id, Lock lock) {
        lockRepository.findById(id).orElseThrow(() -> new LockNotFoundException(id));
        // FIXME update the original entity, do not just replace ids
        lock.setId(id);
        return lockRepository.save(lock);
    }

    public void delete(Integer id) {
        if (!lockRepository.existsById(id)) {
            throw new LockNotFoundException(id);
        }
        lockRepository.deleteById(id);
    }

    public List<Lock> getAllByLockerId(Integer lockerId) {
        if (!lockerRepository.existsById(lockerId)) {
            throw new LockerNotFoundException(lockerId);
        }
        return lockRepository.findByLocker_Id(lockerId);
    }

    public List<Lock> getAllBySkiResortId(Integer skiResortId){
        if (!skiResortRepository.existsById(skiResortId)) {
            throw new SkiResortNotFoundException(skiResortId);
        }
        return lockRepository.findByLocker_SkiResort_Id(skiResortId);
    }

}