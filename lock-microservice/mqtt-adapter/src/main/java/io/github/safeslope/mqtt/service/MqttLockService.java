package io.github.safeslope.mqtt.service;

import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.lock.repository.LockRepository;
import io.github.safeslope.lock.service.LockNotFoundException;
import io.github.safeslope.locker.repository.LockerRepository;
import io.github.safeslope.mqtt.MqttLockAdapter;
import io.github.safeslope.skiresort.repository.SkiResortRepository;
import jakarta.transaction.Transactional;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

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

        try {
            mqtt.sendCommand(tenantId, resortId, lockerId, "{\"cmd\":\"UNLOCK\"}");
        } catch (MqttException e) {
            throw new RuntimeException("Failed sending UNLOCK command via MQTT", e);
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

        try {
            mqtt.sendCommand(tenantId, resortId, lockerId, "{\"cmd\":\"LOCK\"}");
        } catch (MqttException e) {
            throw new RuntimeException("Failed sending LOCK command via MQTT", e);
        }
    }
}