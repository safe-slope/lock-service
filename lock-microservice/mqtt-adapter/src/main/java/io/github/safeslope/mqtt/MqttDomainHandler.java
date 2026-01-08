package io.github.safeslope.mqtt;

import io.github.safeslope.mqtt.dto.MqttMessage;

public interface MqttDomainHandler {
    void onLockEvent(String tenantId, String resortId, String lockerId, MqttMessage dto);
}
