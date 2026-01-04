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

    public void unlock(Integer lockId) throws MqttException {
        Lock lock = lockService.getLock(lockId);

        String tenantId = lock.getLocker().getSkiResort().getId().toString();;
        String lockKey = lock.getMacAddress();

        mqtt.sendCommand(tenantId, lockKey, "{\"cmd\":\"UNLOCK\"}");
    }

    public void lock(Integer lockId) throws MqttException {
        Lock lock = lockService.getLock(lockId);

        String tenantId = lock.getLocker().getSkiResort().getId().toString();;
        String lockKey = lock.getMacAddress();

        mqtt.sendCommand(tenantId, lockKey, "{\"cmd\":\"LOCK\"}");
    }
}
