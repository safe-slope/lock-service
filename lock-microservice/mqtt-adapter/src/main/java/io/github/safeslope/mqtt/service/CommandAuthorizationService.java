package io.github.safeslope.mqtt.service;

import io.github.safeslope.entities.*;
import io.github.safeslope.location.service.LocationService;
import io.github.safeslope.lock.service.LockNotFoundException;
import io.github.safeslope.lock.service.LockService;
import io.github.safeslope.locker.service.LockerService;
import io.github.safeslope.lockevent.service.LockEventService;
import io.github.safeslope.mqtt.s2s.AntiAbuseAction;
import io.github.safeslope.mqtt.s2s.AntiAbuseDecision;
import io.github.safeslope.mqtt.s2s.EvaluateRequest;
import io.github.safeslope.mqtt.s2s.EvaluateResponse;
import io.github.safeslope.mqtt.s2s.VerifyCardRequest;
import io.github.safeslope.mqtt.s2s.VerifyCardResponse;
import io.github.safeslope.mqtt.dto.DtoConstants;
import io.github.safeslope.mqtt.dto.RegistrationDto;
import io.github.safeslope.mqtt.dto.StatusDto;

import org.springframework.beans.factory.annotation.Qualifier;
import io.github.safeslope.skiticket.service.SkiTicketService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;


import java.awt.Point;

@Service
@Transactional
public class CommandAuthorizationService {

    private final LockService lockService;
    private final LockEventService lockEventService;
    private final SkiTicketService skiTicketService;
    private final LockerService lockerService;
    private final LocationService locationService;

    private final RestClient skiCardClient;
    private final Integer resortId;

    private final RestClient antiAbuseClient;



   public CommandAuthorizationService(
            LockService lockService,
            LockEventService lockEventService,
            SkiTicketService skiTicketService,
            LockerService lockerService,
            LocationService locationService,
          
            @Qualifier("skiCardVerificationRestClient") RestClient skiCardClient,
            @Qualifier("antiAbuseRestClient") RestClient antiAbuseClient,
            @Value("${services.ski-card-verification.resort-id}") Integer resortId
    ) {
        this.lockService = lockService;
        this.lockEventService = lockEventService;
        this.skiTicketService = skiTicketService;
        this.lockerService = lockerService;
        this.locationService = locationService;

        this.skiCardClient = skiCardClient;
        this.antiAbuseClient = antiAbuseClient;
        this.resortId = resortId;
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
            default -> {
                return false;
            }
        }
    }

    private boolean antiAbuseVerification(Integer lockId, Integer skiTicketId) {
        if (skiTicketId == null) return false;

        try {
    
            Lock lock = lockService.getLock(lockId);
            Integer lockerId = lock.getLocker() != null ? lock.getLocker().getId() : null;

            AntiAbuseAction action = (lock.getState() == Lock.State.UNLOCKED)
                    ? AntiAbuseAction.LOCK
                    : AntiAbuseAction.UNLOCK;

            var req = new EvaluateRequest(
                    skiTicketId,
                    lockId,
                    lockerId,
                    resortId,
                    action,
                    System.currentTimeMillis()
            );

            var resp = antiAbuseClient.post()
                    .uri("/api/v1/anti-abuse/abuse-verify") 
                    .body(req)
                    .retrieve()
                    .body(EvaluateResponse.class);

            return resp != null && resp.decision() == AntiAbuseDecision.ALLOW;
        } catch (Exception e) {
            return false; 
        }
    }


    private boolean skiTicketVerification(Integer skiTicketId) {
        if (skiTicketId == null) return false;

        try {
            var req = new VerifyCardRequest(skiTicketId.toString(), resortId);

            var resp = skiCardClient.post()
                    .uri("/api/v1/ski-card/verify")
                    .body(req)
                    .retrieve()
                    .body(VerifyCardResponse.class);

            return resp != null && resp.valid();
        } catch (Exception e) {
            return false; // fail-closed
        }
    }
    

    public void persist(StatusDto statusDto) {
        Lock lock = lockService.getLock(statusDto.getLockId());
        SkiTicket skiTicket = null;
        if (statusDto.getSkiTicketId() != null) {
            skiTicket = skiTicketService.get(statusDto.getSkiTicketId());
        }
        LockEvent.EventType eventType = getEventType(statusDto);

        //create a lock entity
        Lock l = Lock.builder()
                .id(lock.getId())
                .macAddress(lock.getMacAddress())
                .dateAdded(lock.getDateAdded())
                .location(lock.getLocation())
                .mode(statusDto.getMode())
                .state(statusDto.getState())
                .locker(lock.getLocker())
                .build();

        //update lock entity in the database
        Lock updatedLock = lockService.update(lock.getId(), l);


        //create a lock event entity
        LockEvent le = LockEvent.builder()
                .lock(updatedLock)
                .eventTime(statusDto.getTimestamp())
                .skiTicket(skiTicket)
                .eventType(eventType)
                .build();

        //persist lock event entity in the database
        lockEventService.create(le);

    }

    private static LockEvent.EventType getEventType(StatusDto statusDto) {
        LockEvent.EventType eventType = null;

        switch (statusDto.getCommand()) {
            case LOCK -> eventType = LockEvent.EventType.LOCK;
            case UNLOCK -> eventType = LockEvent.EventType.UNLOCK;
            case SET_MODE_TO_MAINTENANCE -> eventType = LockEvent.EventType.SET_MODE_TO_MAINTENANCE;
            case SET_MODE_TO_NORMAL -> eventType = LockEvent.EventType.SET_MODE_TO_NORMAL;
            case SET_MODE_TO_DISABLED -> eventType = LockEvent.EventType.SET_MODE_TO_DISABLED;
            case SET_MODE_TO_SERVICE -> eventType = LockEvent.EventType.SET_MODE_TO_SERVICE;
        }

        return eventType;
    }

    public Lock register(RegistrationDto registrationDto) {
        //first check if the lock with a specified mac address exists
        try{
            Lock lock =  lockService.getByMacAddress(registrationDto.getMacAddress());

            //update state of the existing lock

            //create a lock entity
            Lock l = Lock.builder()
                    .id(lock.getId())
                    .macAddress(lock.getMacAddress())
                    .dateAdded(lock.getDateAdded())
                    .location(lock.getLocation())
                    .mode(lock.getMode())
                    .state(registrationDto.getState())
                    .locker(lock.getLocker())
                    .build();

            //update lock entity in the database
            return lockService.update(lock.getId(), l);
        }
        catch (LockNotFoundException e){
            Locker locker = lockerService.get(registrationDto.getLockerId());

            //create a location object
            Point point = new Point(registrationDto.getLat(), registrationDto.getLon());
            Location location = Location.builder()
                    .coordinates(point)
                    .build();

            //persist location in the database
            Location updatedLocation = locationService.create(location);

            //the lock does not yet exist, create a new one and return it
            return lockService.create(
                    Lock.builder()
                            .macAddress(registrationDto.getMacAddress())
                            .locker(locker)
                            .dateAdded(registrationDto.getTimestamp())
                            .state(registrationDto.getState())
                            .mode(Lock.Mode.NORMAL)
                            .location(updatedLocation)
                            .build());
        }


    }

}
