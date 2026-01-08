package io.github.safeslope.mqtt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RequestDto {
    private Integer msgId;
    private Integer lockerId;
    private Integer lockId;
    private Integer skiTicketId;
    private String command;
    private LocalDateTime timestamp;
}
