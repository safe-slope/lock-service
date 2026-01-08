package io.github.safeslope.mqtt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RegistrationDto {
    private Integer msgId;
    private Integer lockerId;
    private String macAddress;
    private String state;
    private LocalDateTime timestamp;
}
