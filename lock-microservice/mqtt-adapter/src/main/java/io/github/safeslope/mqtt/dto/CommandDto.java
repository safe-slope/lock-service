package io.github.safeslope.mqtt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommandDto {
    private Integer msgId;
    private String command;
    private Integer lockerId;
    private Integer lockId;
    private LocalDateTime timestamp;
}
