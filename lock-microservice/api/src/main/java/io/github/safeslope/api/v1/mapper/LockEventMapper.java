package io.github.safeslope.api.v1.mapper;

import io.github.safeslope.api.v1.dto.LockEventDto;
import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.LockEvent;
import io.github.safeslope.entities.SkiTicket;
import io.github.safeslope.lock.service.LockService;
import io.github.safeslope.skiticket.service.SkiTicketService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class LockEventMapper {

    protected LockService lockService;

    protected SkiTicketService skiTicketService;

    @Mapping(target = "lockId", source = "lock.id")
    @Mapping(target = "skiTicketId", source = "skiTicket.id")
    public abstract LockEventDto toDto(LockEvent entity);

    @Mapping(target = "lock", ignore = true)
    @Mapping(target = "skiTicket", ignore = true)
    public abstract LockEvent toEntity(LockEventDto dto);

    @AfterMapping
    protected void afterToEntity(LockEventDto dto, @MappingTarget LockEvent entity) {
        Integer lockId = dto.getLockId();
        if (lockId != null) {
            Lock lock = lockService.getLock(lockId); // may throw LockNotFoundException
            entity.setLock(lock);
        } else {
            entity.setLock(null);
        }

        Integer ticketId = dto.getSkiTicketId();
        if (ticketId != null) {
            SkiTicket ticket = skiTicketService.get(ticketId); // may throw SkiTicketNotFoundException
            entity.setSkiTicket(ticket);
        } else {
            entity.setSkiTicket(null);
        }
    }
}
