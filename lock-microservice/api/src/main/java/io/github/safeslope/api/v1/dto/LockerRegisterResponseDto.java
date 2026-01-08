package io.github.safeslope.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockerRegisterResponseDto {
    private Integer lockerId;
    private Integer skiResortId;
    private Integer tenantId;
}
