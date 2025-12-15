package io.github.safeslope.api.v1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockEventDto {
    private Integer id;
    private Timestamp eventTime;
    private String eventType;
    private Integer lockId;
    private Integer skiTicketId;
}