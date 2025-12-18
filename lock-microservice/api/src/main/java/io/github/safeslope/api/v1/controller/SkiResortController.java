package io.github.safeslope.api.v1.controller;


import io.github.safeslope.api.v1.dto.LockDto;
import io.github.safeslope.api.v1.dto.LockEventDto;
import io.github.safeslope.api.v1.dto.LockerDto;
import io.github.safeslope.api.v1.dto.SkiResortDto;
import io.github.safeslope.api.v1.mapper.LockEventMapper;
import io.github.safeslope.api.v1.mapper.LockMapper;
import io.github.safeslope.api.v1.mapper.LockerMapper;
import io.github.safeslope.api.v1.mapper.SkiResortMapper;
import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.lock.service.LockService;
import io.github.safeslope.locker.service.LockerService;
import io.github.safeslope.lockevent.service.LockEventService;
import io.github.safeslope.skiresort.service.SkiResortService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/skiresorts")
public class SkiResortController {

    private final SkiResortService skiResortService;
    private final SkiResortMapper skiResortMapper;
    private final LockerService lockerService;
    private final LockerMapper lockerMapper;
    private final LockEventService lockEventService;
    private final LockEventMapper lockEventMapper;
    private final LockService lockService;
    private final LockMapper lockMapper;

    public SkiResortController(
            SkiResortService skiResortService,
            SkiResortMapper skiResortMapper,
            LockerService lockerService,
            LockerMapper lockerMapper,
            LockEventService lockEventService,
            LockEventMapper lockEventMapper,
            LockService lockService,
            LockMapper lockMapper) {
        this.skiResortService = skiResortService;
        this.skiResortMapper = skiResortMapper;
        this.lockerService = lockerService;
        this.lockerMapper = lockerMapper;
        this.lockEventService = lockEventService;
        this.lockEventMapper = lockEventMapper;
        this.lockService = lockService;
        this.lockMapper = lockMapper;
    }

    @GetMapping
    public List<SkiResortDto> list() {
        return skiResortService.getAll().stream().map(skiResortMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SkiResortDto get(@PathVariable Integer id) {
        return skiResortMapper.toDto(skiResortService.get(id));
    }

    @GetMapping("/name/{name}")
    public SkiResortDto getByName(@PathVariable String name) {
        return skiResortMapper.toDto(skiResortService.getByName(name));
    }

    @GetMapping("/{id}/lockers")
    public List<LockerDto> getLockers(@PathVariable Integer id) {
        return lockerMapper.toDtoList(lockerService.getAllBySkiResortId(id));
    }

    @GetMapping("/{id}/locks")
    public List<LockDto> getLocks(@PathVariable Integer id) {
        return lockMapper.toDtoList(lockService.getAllBySkiResortId(id));
    }

    @GetMapping("/{id}/lock-events")
    public List<LockEventDto> getLockEvents(@PathVariable Integer id) {
        return lockEventMapper.toDtoList(lockEventService.getAllBySkiResort(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        skiResortService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<SkiResortDto> create(@RequestBody SkiResortDto dto) {
        SkiResort entity = skiResortMapper.toEntity(dto);
        SkiResort saved = skiResortService.create(entity);
        return ResponseEntity.ok(skiResortMapper.toDto(saved));
    }
}
