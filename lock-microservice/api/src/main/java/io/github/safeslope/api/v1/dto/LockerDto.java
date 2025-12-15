package io.github.safeslope.api.v1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockerDto {
    private Integer id;
    private LocalDateTime dateAdded;
    private String macAddress;
    private Integer skiResortId;
}