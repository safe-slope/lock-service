package io.github.safeslope.api.v1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkiTicketDto {
    private Integer id;
    private String ticketNumber;
    private Timestamp validFrom;
    private Timestamp validUntil;
    private Integer skiResortId;
}