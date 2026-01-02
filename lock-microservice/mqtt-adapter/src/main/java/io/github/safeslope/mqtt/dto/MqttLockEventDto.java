package io.github.safeslope.mqtt.dto;

import lombok.Getter;
import lombok.Setter;

/*
 Payload:  lock -> backend (MQTT event message).
 */
@Getter
@Setter
public class MqttLockEventDto {
    private String type;
    private String ts;
}
