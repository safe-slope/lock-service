package io.github.safeslope.mqtt.dto;

import io.github.safeslope.entities.Lock;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RegistrationDto {
    private UUID msgId;
    private Integer lockerId;
    private String macAddress;
    private Lock.State state;
    private LocalDateTime timestamp;
}
