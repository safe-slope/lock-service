package io.github.safeslope.api.v1.controller;

import io.github.safeslope.api.v1.dto.LockEventDto;
import io.github.safeslope.api.v1.dto.SkiTicketDto;
import io.github.safeslope.api.v1.mapper.LockEventMapper;
import io.github.safeslope.api.v1.mapper.SkiTicketMapper;
import io.github.safeslope.lockevent.service.LockEventService;
import io.github.safeslope.skiticket.service.SkiTicketService;
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
    public List<SkiTicketDto> list() {
        return skiTicketMapper.toDtoList(skiTicketService.getAll());
    }

    @GetMapping("/{id}")
    public SkiTicketDto get(@PathVariable Integer id) {
        return skiTicketMapper.toDto(skiTicketService.get(id));
    }

    @GetMapping("{id}/lock-events")
    public List<LockEventDto> getLockEvents(@PathVariable Integer id) {
        return lockEventMapper.toDtoList(lockEventService.getAllBySkiTicket(id));
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
