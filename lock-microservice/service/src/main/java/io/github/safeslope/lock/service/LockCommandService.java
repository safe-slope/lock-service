package io.github.safeslope.lock.service;

import io.github.safeslope.entities.Lock;
import org.springframework.stereotype.Service;

@Service
public class LockCommandService {

    private final LockService lockService;

    public LockCommandService(LockService lockService) {
        this.lockService = lockService;
    }


    public void unlockByLockId(Integer lockId) {
        unlock(lockId);
    }


    public void lockByLockId(Integer lockId) {
        lock(lockId);
    }

    public void unlock(Integer lockId) {
        Lock lock = lockService.getLock(lockId);
        String tenantId = lock.getLocker().getSkiResort().getId().toString();
        String lockKey = lock.getMacAddress();

        // TODO: mqtt-adapter publish UNLOCK
    }

    public void lock(Integer lockId) {
        Lock lock = lockService.getLock(lockId);
        String tenantId = lock.getLocker().getSkiResort().getId().toString();
        String lockKey = lock.getMacAddress();

        // TODO: mqtt-adapter publish LOCK
    }
}
