package io.github.safeslope.lock.service;

import io.github.safeslope.entities.Lock;
import io.github.safeslope.mqtt.MqttLockAdapter;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

@Service
public class LockCommandService {

    private final LockService lockService;
    private final MqttLockAdapter mqtt;

    public LockCommandService(LockService lockService, MqttLockAdapter mqtt) {
        this.lockService = lockService;
        this.mqtt = mqtt;
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

        try {
            mqtt.sendCommand(tenantId, lockKey, "{\"cmd\":\"UNLOCK\"}");
        } catch (MqttException e) {
            throw new RuntimeException("Failed sending UNLOCK command via MQTT", e);
        }
    }

    public void lock(Integer lockId) {
        Lock lock = lockService.getLock(lockId);
        String tenantId = lock.getLocker().getSkiResort().getId().toString();
        String lockKey = lock.getMacAddress();

        try {
            mqtt.sendCommand(tenantId, lockKey, "{\"cmd\":\"LOCK\"}");
        } catch (MqttException e) {
            throw new RuntimeException("Failed sending LOCK command via MQTT", e);
        }
    }
}
