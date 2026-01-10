package io.github.safeslope.mqtt.service;

import io.github.safeslope.entities.Lock;
import io.github.safeslope.lock.service.LockService;
import io.github.safeslope.mqtt.dto.DtoConstants;
import org.springframework.stereotype.Service;

@Service
public class CommandAuthorizationService {

    private final LockService lockService;

    public CommandAuthorizationService(LockService lockService) {
        this.lockService = lockService;
    }


    public boolean authorize(Integer lockId, DtoConstants.Command command, Integer skiTicketId) {
        Lock lock = lockService.getLock(lockId);

        switch (command) {
            case LOCK -> {
                //check that the lock is in normal mode and the state is unlocked
                return lock.getMode() == Lock.Mode.NORMAL &&
                        lock.getState() == Lock.State.UNLOCKED &&
                        antiAbuseVerification(lockId, skiTicketId) &&
                        skiTicketVerification(skiTicketId);
            }
            case UNLOCK -> {
                //check that the lock is in normal mode and the state is locked
                return lock.getMode() == Lock.Mode.NORMAL &&
                        lock.getState() == Lock.State.LOCKED &&
                        antiAbuseVerification(lockId, skiTicketId) &&
                        skiTicketVerification(skiTicketId);
            }
            case SET_MODE_TO_NORMAL -> {
                //check that the lock is not in normal mode
                return lock.getMode() != Lock.Mode.NORMAL;
            }
            case SET_MODE_TO_SERVICE -> {
                //check that the lock is not in service mode
                return lock.getMode() != Lock.Mode.SERVICE;
            }
            case SET_MODE_TO_DISABLED -> {
                //check that the lock is not in disabled mode
                return lock.getMode() != Lock.Mode.DISABLED;
            }
            case SET_MODE_TO_MAINTENANCE -> {
                //check that the lock is not in maintenance mode
                return lock.getMode() != Lock.Mode.MAINTENANCE;
            }
            case null, default -> {
                return false;
            }
        }
    }

    private boolean antiAbuseVerification(Integer lockId, Integer skiTicketId) {
        // TODO add api call to the anti-abuse service
        return true;
    }

    private boolean skiTicketVerification(Integer skiTicketId) {
        // TODO add api call to the ski-card-verification service
        return true;
    }

}
