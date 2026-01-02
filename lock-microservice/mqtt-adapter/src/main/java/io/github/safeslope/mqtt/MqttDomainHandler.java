package io.github.safeslope.mqtt;

import io.github.safeslope.mqtt.dto.MqttLockEventDto;

public interface MqttDomainHandler {
    void onLockEvent(String tenantId, String lockKey, MqttLockEventDto dto);
}
