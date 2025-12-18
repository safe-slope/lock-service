package io.github.safeslope.api.v1.mapper;

import io.github.safeslope.api.v1.dto.SkiTicketDto;
import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.entities.SkiTicket;
import io.github.safeslope.skiresort.service.SkiResortService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class SkiTicketMapper {

    protected SkiResortService skiResortService;

    @Mapping(target = "skiResortId", source = "skiResort.id")
    public abstract SkiTicketDto toDto(SkiTicket entity);

    @Mapping(target = "skiResort", ignore = true)
    public abstract SkiTicket toEntity(SkiTicketDto dto);

    @AfterMapping
    protected void afterToEntity(SkiTicketDto dto, @MappingTarget SkiTicket entity) {
        Integer resortId = dto.getSkiResortId();
        if (resortId != null) {
            SkiResort resort = skiResortService.get(resortId); // may throw SkiResortNotFoundException
            entity.setSkiResort(resort);
        } else {
            entity.setSkiResort(null);
        }
    }
}
