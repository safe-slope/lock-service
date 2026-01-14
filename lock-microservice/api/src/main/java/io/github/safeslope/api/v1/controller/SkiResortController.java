package io.github.safeslope.api.v1.controller;


import io.github.safeslope.api.v1.dto.*;
import io.github.safeslope.api.v1.mapper.*;
import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.lock.service.LockService;
import io.github.safeslope.locker.service.LockerService;
import io.github.safeslope.lockevent.service.LockEventService;
import io.github.safeslope.skiresort.service.SkiResortService;
import io.github.safeslope.skiticket.service.SkiTicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ski-resorts")
public class SkiResortController {

    private final SkiResortService skiResortService;
    private final SkiResortMapper skiResortMapper;
    private final LockerService lockerService;
    private final LockerMapper lockerMapper;
    private final LockEventService lockEventService;
    private final LockEventMapper lockEventMapper;
    private final LockService lockService;
    private final LockMapper lockMapper;
    private final SkiTicketService skiTicketService;
    private final SkiTicketMapper skiTicketMapper;

    public SkiResortController(
            SkiResortService skiResortService,
            SkiResortMapper skiResortMapper,
            LockerService lockerService,
            LockerMapper lockerMapper,
            LockEventService lockEventService,
            LockEventMapper lockEventMapper,
            LockService lockService,
            LockMapper lockMapper, SkiTicketService skiTicketService, SkiTicketMapper skiTicketMapper) {
        this.skiResortService = skiResortService;
        this.skiResortMapper = skiResortMapper;
        this.lockerService = lockerService;
        this.lockerMapper = lockerMapper;
        this.lockEventService = lockEventService;
        this.lockEventMapper = lockEventMapper;
        this.lockService = lockService;
        this.lockMapper = lockMapper;
        this.skiTicketService = skiTicketService;
        this.skiTicketMapper = skiTicketMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public Page<SkiResortDto> list(Pageable pageable) {
        return skiResortService.getAll(pageable).map(skiResortMapper::toDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public SkiResortDto get(@PathVariable Integer id) {
        return skiResortMapper.toDto(skiResortService.get(id));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public SkiResortDto getByName(@PathVariable String name) {
        return skiResortMapper.toDto(skiResortService.getByName(name));
    }

    @GetMapping("/{id}/lockers")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public Page<LockerDto> getLockers(@PathVariable Integer id, Pageable pageable) {
        return lockerService.getAllBySkiResortId(id, pageable).map(lockerMapper::toDto);
    }

    @GetMapping("/{id}/locks")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public Page<LockDto> getLocks(@PathVariable Integer id, Pageable pageable) {
        return lockService.getAllBySkiResortId(id, pageable).map(lockMapper::toDto);
    }

    @GetMapping("/{id}/lock-events")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public Page<LockEventDto> getLockEvents(@PathVariable Integer id, Pageable pageable) {
        return lockEventService.getAllBySkiResort(id, pageable).map(lockEventMapper::toDto);
    }

    @GetMapping("{id}/ski-tickets")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public Page<SkiTicketDto> getSkiTickets(@PathVariable Integer id, Pageable pageable) {
        return skiTicketService.getAllBySkiResortId(id, pageable).map(skiTicketMapper::toDto);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        skiResortService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<SkiResortDto> create(@RequestBody SkiResortDto dto) {
        SkiResort saved = skiResortService.create(skiResortMapper.toEntity(dto));

        return ResponseEntity
                .created(URI.create("/api/v1/skiresorts/" + saved.getId()))
                .body(skiResortMapper.toDto(saved));
    }

}
