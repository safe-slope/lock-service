package io.github.safeslope.api.v1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockerDto {
    private Integer id;
    private Timestamp dateAdded;
    private String macAddress;
    private Integer skiResortId;
}