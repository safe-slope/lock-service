package io.github.safeslope.mqtt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommandDto {
    private Integer msgId;
    private Integer lockerId;
    private Integer lockId;
    private DtoConstants.Command command;
    private LocalDateTime timestamp;
}
