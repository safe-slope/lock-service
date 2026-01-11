package io.github.safeslope.mqtt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CommandDto {
    private UUID msgId;
    private Integer lockerId;
    private Integer lockId;
    private Integer skiTicketId;
    private DtoConstants.Command command;
    private LocalDateTime timestamp;
}
