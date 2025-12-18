package io.github.safeslope.api.v1.mapper;

import io.github.safeslope.api.v1.dto.LockDto;
import io.github.safeslope.entities.Location;
import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.Locker;
import io.github.safeslope.location.service.LocationService;
import io.github.safeslope.locker.service.LockerService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class LockMapper {
    protected LockerService lockerService;
    protected LocationService locationService;

    @Mapping(target = "lockerId", source = "locker.id")
    @Mapping(target = "locationId", source = "location.id")
    public abstract LockDto toDto(Lock entity);

    public abstract List<LockDto> toDtoList(List<Lock> entities);

    @Mapping(target = "locker", ignore = true)
    @Mapping(target = "location", ignore = true)
    public abstract Lock toEntity(LockDto dto);

    public abstract List<Lock> toEntityList(List<LockDto> dtos);

    @AfterMapping
    protected void afterToEntity(LockDto dto, @MappingTarget Lock entity) {
        Integer lockerId = dto.getLockerId();
        if (lockerId != null) {
            Locker locker = lockerService.get(lockerId); // may throw LockerNotFoundException
            entity.setLocker(locker);
        } else {
            entity.setLocker(null);
        }

        Integer locationId = dto.getLocationId();
        if (locationId != null) {
            Location loc = locationService.get(locationId); // may throw LocationNotFoundException
            entity.setLocation(loc);
        } else {
            entity.setLocation(null);
        }
    }

}
