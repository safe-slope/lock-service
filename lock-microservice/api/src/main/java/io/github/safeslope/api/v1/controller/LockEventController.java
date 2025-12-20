package io.github.safeslope.api.v1.controller;


import io.github.safeslope.api.v1.dto.LockEventDto;
import io.github.safeslope.entities.LockEvent;
import io.github.safeslope.lockevent.service.LockEventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lock-events")
public class LockEventController {

    private final LockEventService lockEventService;

    public LockEventController(LockEventService lockEventService) {
        this.lockEventService = lockEventService;
    }

    @GetMapping
    public List<LockEventDto> list() {
        return lockEventService.getAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LockEventDto get(@PathVariable Integer id) {
        return toDto(lockEventService.get(id));
    }


    private LockEventDto toDto(LockEvent le) {
        return new LockEventDto(
                le.getId(),
                le.getEventTime(),
                le.getEventType().name(),
                le.getLock() != null ? le.getLock().getId() : null,
                le.getSkiTicket() != null ? le.getSkiTicket().getId() : null
        );
    }
}
