package io.github.safeslope.mqtt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.lock.repository.LockRepository;
import io.github.safeslope.lock.service.LockNotFoundException;
import io.github.safeslope.locker.repository.LockerRepository;
import io.github.safeslope.mqtt.MqttLockAdapter;
import io.github.safeslope.mqtt.dto.CommandDto;
import io.github.safeslope.mqtt.dto.DtoConstants;
import io.github.safeslope.skiresort.repository.SkiResortRepository;
import jakarta.transaction.Transactional;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class MqttLockService {
    private final LockRepository lockRepository;
    private final MqttLockAdapter mqtt;

    public MqttLockService(LockRepository lockRepository, MqttLockAdapter mqtt) {
        this.lockRepository = lockRepository;
        this.mqtt = mqtt;
    }

    public void unlock(String mac) {
        Lock lock = lockRepository.findByMacAddress(mac);

        if(lock == null){
            throw new LockNotFoundException(mac);
        }

        SkiResort skiResort = lock.getLocker().getSkiResort();
        Integer tenantId = skiResort.getTenantId();
        Integer resortId = skiResort.getId();
        Integer lockerId = lock.getLocker().getId();
        Integer lockId = lock.getId();

        LocalDateTime now = LocalDateTime.now();

        CommandDto commandDto = new CommandDto(
                UUID.randomUUID(),
                lockerId,
                lockId,
                null,
                DtoConstants.Command.UNLOCK,
                now
        );

        try {
            mqtt.sendCommand(tenantId, resortId, lockerId, commandDto);
        } catch (MqttException e) {
            throw new RuntimeException("Failed sending UNLOCK command via MQTT", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void lock(String mac) throws MqttException {
        Lock lock = lockRepository.findByMacAddress(mac);

        if(lock == null){
            throw new LockNotFoundException(mac);
        }

        SkiResort skiResort = lock.getLocker().getSkiResort();
        Integer tenantId = skiResort.getTenantId();
        Integer resortId = skiResort.getId();
        Integer lockerId = lock.getLocker().getId();
        Integer lockId = lock.getId();

        LocalDateTime now = LocalDateTime.now();

        CommandDto commandDto = new CommandDto(
                UUID.randomUUID(),
                lockerId,
                lockId,
                null,
                DtoConstants.Command.LOCK,
                now
        );

        try {
            mqtt.sendCommand(tenantId, resortId, lockerId, commandDto);
        } catch (MqttException e) {
            throw new RuntimeException("Failed sending UNLOCK command via MQTT", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}