package io.github.safeslope.api.v1.controller;

import io.github.safeslope.api.v1.dto.LockEventDto;
import io.github.safeslope.api.v1.dto.SkiTicketDto;
import io.github.safeslope.api.v1.mapper.LockEventMapper;
import io.github.safeslope.api.v1.mapper.SkiTicketMapper;
import io.github.safeslope.lockevent.service.LockEventService;
import io.github.safeslope.skiticket.service.SkiTicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ski-tickets")
public class SkiTicketController {

    private final SkiTicketService skiTicketService;
    private final SkiTicketMapper skiTicketMapper;
    private final LockEventService lockEventService;
    private final LockEventMapper lockEventMapper;

    public SkiTicketController(
            SkiTicketService skiTicketService,
            SkiTicketMapper skiTicketMapper,
            LockEventService lockEventService,
            LockEventMapper lockEventMapper) {
        this.skiTicketService = skiTicketService;
        this.skiTicketMapper = skiTicketMapper;
        this.lockEventService = lockEventService;
        this.lockEventMapper = lockEventMapper;
    }

    @GetMapping
    public Page<SkiTicketDto> list(Pageable pageable) {
        return skiTicketService.getAll(pageable).map(skiTicketMapper::toDto);
    }

    @GetMapping("/{id}")
    public SkiTicketDto get(@PathVariable Integer id) {
        return skiTicketMapper.toDto(skiTicketService.get(id));
    }

    @GetMapping("{id}/lock-events")
    public Page<LockEventDto> getLockEvents(@PathVariable Integer id, Pageable pageable) {
        return lockEventService.getAllBySkiTicket(id, pageable).map(lockEventMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        skiTicketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public SkiTicketDto create(@RequestBody SkiTicketDto dto) {
        return skiTicketMapper.toDto(skiTicketService.create(skiTicketMapper.toEntity(dto)));
    }
}
