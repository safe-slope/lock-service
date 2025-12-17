package io.github.safeslope.api.v1.controller;

import io.github.safeslope.api.v1.dto.LockDto;
import io.github.safeslope.entities.Lock;
import io.github.safeslope.lock.service.LockService;
import io.github.safeslope.locker.service.LockerService;
import io.github.safeslope.location.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/locks")
public class LockController {

    private final LockService lockService;

    public LockController(LockService lockService, LockerService lockerService, LocationService locationService) {
        this.lockService = lockService;
    }

    @GetMapping
    public List<LockDto> list() {
        return lockService.getAllLocks().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LockDto get(@PathVariable Integer id) {
        return toDto(lockService.getLock(id));
    }

    @GetMapping("/mac/{mac}")
    public LockDto getByMac(@PathVariable String mac) {
        return toDto(lockService.getByMacAddress(mac));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        lockService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private LockDto toDto(Lock l) {
        return new LockDto(
                l.getId(),
                l.getDateAdded(),
                l.getMacAddress(),
                l.getLocker() != null ? l.getLocker().getId() : null,
                l.getLocation() != null ? l.getLocation().getId() : null
        );
    }
}