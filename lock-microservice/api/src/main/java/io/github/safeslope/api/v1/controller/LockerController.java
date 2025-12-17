package io.github.safeslope.api.v1.controller;

import io.github.safeslope.api.v1.dto.LockerDto;
import io.github.safeslope.entities.Locker;
import io.github.safeslope.locker.service.LockerService;
import io.github.safeslope.skiresort.service.SkiResortService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lockers")
public class LockerController {

    private final LockerService lockerService;

    public LockerController(LockerService lockerService, SkiResortService skiResortService) {
        this.lockerService = lockerService;
    }

    @GetMapping
    public List<LockerDto> list() {
        return lockerService.getAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LockerDto get(@PathVariable Integer id) {
        return toDto(lockerService.get(id));
    }

    @GetMapping("/mac/{mac}")
    public LockerDto getByMac(@PathVariable String mac) {
        return toDto(lockerService.getByMacAddress(mac));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        lockerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private LockerDto toDto(Locker l) {
        return new LockerDto(
                l.getId(),
                l.getDateAdded(),
                l.getMacAddress(),
                l.getSkiResort() != null ? l.getSkiResort().getId() : null
        );
    }
}