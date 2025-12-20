package io.github.safeslope.api.v1.mapper;

import io.github.safeslope.api.v1.dto.LockDto;
import io.github.safeslope.api.v1.dto.LockerDto;
import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.Locker;
import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.skiresort.service.SkiResortService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class LockerMapper {
    protected SkiResortService skiResortService;

    @Mapping(target = "skiResortId", source = "skiResort.id")
    public abstract LockerDto toDto(Locker entity);

    public abstract List<LockerDto> toDtoList(List<Locker> entities);

    @Mapping(target = "skiResort", ignore = true)
    public abstract Locker toEntity(LockerDto dto);

    public abstract List<Locker> toEntityList(List<LockerDto> dtos);

    @AfterMapping
    protected void afterToEntity(LockerDto dto, @MappingTarget Locker entity) {
        Integer resortId = dto.getSkiResortId();
        if (resortId != null) {
            SkiResort resort = skiResortService.get(resortId); // may throw SkiResortNotFoundException
            entity.setSkiResort(resort);
        } else {
            entity.setSkiResort(null);
        }
    }

}
