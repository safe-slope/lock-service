package io.github.safeslope.api.v1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockDto {
    private Integer id;
    private LocalDateTime dateAdded;

    @NotBlank
    private String macAddress;

    @NotNull
    private Integer lockerId;

    private Integer locationId;
}