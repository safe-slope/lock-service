package io.github.safeslope.mqtt.dto;

import io.github.safeslope.entities.Lock;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class StatusDto {
    private UUID msgId;
    private Integer lockerId;
    private Integer lockId;
    private LocalDateTime timestamp;
    private Lock.State state;
    private Lock.Mode mode;
    private DtoConstants.Status status;
}
