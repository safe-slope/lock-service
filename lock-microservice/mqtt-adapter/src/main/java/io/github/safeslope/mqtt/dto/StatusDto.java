package io.github.safeslope.mqtt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StatusDto {
    private Integer msgId;
    private Integer lockerId;
    private Integer lockId;
    private LocalDateTime timestamp;
    private String state;
    private String status;
    private String mode;
}
