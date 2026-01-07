package io.github.safeslope.lock.service;

import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.mqtt.MqttLockAdapter;
import io.github.safeslope.skiresort.service.SkiResortService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

@Service
public class LockCommandService {

    private final LockService lockService;
    private final SkiResortService skiResortService;
    private final MqttLockAdapter mqtt;

    public LockCommandService(LockService lockService, MqttLockAdapter mqtt) {
        this.lockService = lockService;
        this.mqtt = mqtt;
    }

    public void unlock(String mac) throws MqttException {
        // iz MAC dobi id ključavnice, tenant ID
        //      preveri če obstaja, če ne jih temu primerno ustvari (nedodeljen locker in lock z dodeljenim lockerjem)
        // preveri trenutno stanje ključavnice
        // pokliči anti-abuse
        // pokliči skicard-verification
        // dodaj nov lock-event v bazo
        // pošlji ukaz nazaj ključavnici

        Lock lock = lockService.getByMacAddress(mac);
        SkiResort skiResort = lock.getLocker().getSkiResort();
        Integer tenantId = skiResort.getTenantId();
        String lockKey = lock.getMacAddress();

        try {
            mqtt.sendCommand(tenantId, lockKey, "{\"cmd\":\"UNLOCK\"}");
        } catch (MqttException e) {
            throw new RuntimeException("Failed sending UNLOCK command via MQTT", e);
        }
    }

    public void lock(String mac) throws MqttException {
        // iz MAC dobi id ključavnice, tenant ID
        //      preveri če obstaja, če ne jih temu primerno ustvari (nedodeljen locker in lock z dodeljenim lockerjem)
        // preveri trenutno stanje ključavnice
        // pokliči anti-abuse
        // pokliči skicard-verification
        // dodaj nov lock-event v bazo
        // pošlji ukaz nazaj ključavnici

        Lock lock = lockService.getByMacAddress(mac);
        SkiResort skiResort = lock.getLocker().getSkiResort();
        Integer tenantId = skiResort.getTenantId();
        String lockKey = lock.getMacAddress();

        try {
            mqtt.sendCommand(tenantId, lockKey, "{\"cmd\":\"LOCK\"}");
        } catch (MqttException e) {
            throw new RuntimeException("Failed sending LOCK command via MQTT", e);
        }
    }
}
