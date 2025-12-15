package io.github.safeslope.api.v1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkiTicketDto {
    private Integer id;
    private String ticketNumber;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private Integer skiResortId;
}