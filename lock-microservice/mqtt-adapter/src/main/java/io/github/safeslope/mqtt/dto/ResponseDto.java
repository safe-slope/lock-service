package io.github.safeslope.mqtt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResponseDto {
    private UUID msgId;
    private String status;
    private String lockMacAddress;
    private Integer lockId;
    private Integer lockerId;
    private LocalDateTime timestamp;
}
