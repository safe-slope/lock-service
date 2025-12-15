package io.github.safeslope.api.v1.controller;

import io.github.safeslope.api.ApiConstants;
import io.github.safeslope.api.v1.dto.LockDto;
import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.Locker;
import io.github.safeslope.entities.Location;
import io.github.safeslope.lock.service.LockService;
import io.github.safeslope.locker.service.LockerService;
import io.github.safeslope.location.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiConstants.V1 + "/locks")
public class LockController {

    private final LockService lockService;
    private final LockerService lockerService;
    private final LocationService locationService;

    public LockController(LockService lockService, LockerService lockerService, LocationService locationService) {
        this.lockService = lockService;
        this.lockerService = lockerService;
        this.locationService = locationService;
    }

    @GetMapping
    public List<LockDto> list() {
        return lockService.getAllLocks().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LockDto get(@PathVariable Integer id) {
        return toDto(lockService.getLock(id));
    }

    @PostMapping
    public ResponseEntity<LockDto> create(@Valid @RequestBody LockDto dto) {
        Lock entity = fromDto(dto);
        Locker locker = lockerService.get(dto.getLockerId());
        entity.setLocker(locker);
        if (dto.getLocationId() != null) {
            Location loc = locationService.get(dto.getLocationId());
            entity.setLocation(loc);
        }
        Lock saved = lockService.create(entity);
        LockDto out = toDto(saved);
        return ResponseEntity.created(URI.create(ApiConstants.V1 + "/locks/" + out.getId())).body(out);
    }

    @PutMapping("/{id}")
    public LockDto update(@PathVariable Integer id, @Valid @RequestBody LockDto dto) {
        Lock existing = lockService.getLock(id);
        existing.setMacAddress(dto.getMacAddress());
        if (!existing.getLocker().getId().equals(dto.getLockerId())) {
            existing.setLocker(lockerService.get(dto.getLockerId()));
        }
        if (dto.getLocationId() != null) {
            existing.setLocation(locationService.get(dto.getLocationId()));
        } else {
            existing.setLocation(null);
        }
        return toDto(lockService.create(existing));
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

    private Lock fromDto(LockDto d) {
        Lock l = new Lock();
        l.setMacAddress(d.getMacAddress());
        return l;
    }
}