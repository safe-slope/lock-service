package io.github.safeslope.api.v1.mapper;

import io.github.safeslope.api.v1.dto.SkiResortDto;
import io.github.safeslope.entities.SkiResort;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class SkiResortMapper {

    public abstract SkiResortDto toDto(SkiResort entity);

    public abstract SkiResort toEntity(SkiResortDto dto);
}
