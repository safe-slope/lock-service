package io.github.safeslope.api.v1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkiResortDto {
    private Integer id;

    private Integer tenantId;

    @NotBlank
    private String name;

    private String address;
}