package io.github.safeslope.api.v1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockEventDto {
    private Integer id;
    private LocalDateTime eventTime;
    private String eventType;
    private Integer lockId;
    private Integer skiTicketId;
}