package io.github.safeslope.mqtt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommandDto {
    private Integer msgId;
    private Command command;
    private Integer lockerId;
    private Integer lockId;
    private LocalDateTime timestamp;

    private enum Command {
        LOCK,
        UNLOCK,
        SET_MODE_TO_NORMAL,
        SET_MODE_TO_SERVICE,
        SET_MODE_TO_MAINTENANCE,
        SET_MODE_TO_DISABLED,
    }
}
