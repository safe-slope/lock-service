package io.github.safeslope.api.v1.controller;

import io.github.safeslope.api.v1.dto.LockDto;
import io.github.safeslope.api.v1.dto.LockerDto;
import io.github.safeslope.entities.Locker;
import io.github.safeslope.entities.SkiResort;
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
    private final SkiResortService skiResortService;

    public LockerController(LockerService lockerService, SkiResortService skiResortService) {
        this.lockerService = lockerService;
        this.skiResortService = skiResortService;
    }

    @GetMapping
    public List<LockerDto> list() {
        return lockerService.getAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LockerDto get(@PathVariable Integer id) {
        return toDto(lockerService.get(id));
    }

    @PutMapping("/{id}")
    public LockerDto update(@PathVariable Integer id, @RequestBody LockerDto dto) {
        return toDto(lockerService.update(id, fromDto(dto)));
    }

    @GetMapping("/mac/{mac}")
    public LockerDto getByMac(@PathVariable String mac) {
        return toDto(lockerService.getByMacAddress(mac));
    }

    @GetMapping("/{id}/locks")
    public List<LockDto> getLocks(@PathVariable Integer id) {
        // TODO klici metodo v LockerService
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

    private Locker fromDto(LockerDto ld) {
        SkiResort resort = null;
        if (ld.getSkiResortId() != null) {
            resort = skiResortService.get(ld.getSkiResortId());
        }

        return Locker.builder()
                .macAddress(ld.getMacAddress())
                .dateAdded(ld.getDateAdded())
                .skiResort(resort)
                .build();
    }
}