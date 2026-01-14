package io.github.safeslope.api.v1.controller;

import io.github.safeslope.api.v1.dto.LockDto;
import io.github.safeslope.api.v1.dto.LockerDto;
import io.github.safeslope.api.v1.dto.LockerRegisterDto;
import io.github.safeslope.api.v1.dto.LockerRegisterResponseDto;
import io.github.safeslope.api.v1.mapper.LockMapper;
import io.github.safeslope.api.v1.mapper.LockerMapper;
import io.github.safeslope.entities.Locker;
import io.github.safeslope.lock.service.LockService;
import io.github.safeslope.locker.service.LockerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lockers")
public class LockerController {

    private final LockerService lockerService;
    private final LockerMapper lockerMapper;
    private final LockService lockService;
    private final LockMapper lockMapper;


    public LockerController(LockerService lockerService, LockerMapper lockerMapper, LockService lockService, LockMapper lockMapper) {
        this.lockerService = lockerService;
        this.lockerMapper = lockerMapper;
        this.lockService = lockService;
        this.lockMapper = lockMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public Page<LockerDto> list(Pageable pageable) {
        return lockerService.getAll(pageable).map(lockerMapper::toDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public LockerDto get(@PathVariable Integer id) {
        return lockerMapper.toDto(lockerService.get(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public LockerDto update(@PathVariable Integer id, @RequestBody LockerDto dto) {
        return lockerMapper.toDto(lockerService.update(id, lockerMapper.toEntity(dto)));
    }

    @GetMapping("/unassgined")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public Page<LockerDto> getUnassigned(Pageable pageable) {
        return lockerService.getAllUnassigned(pageable).map(lockerMapper::toDto);
    }

    @GetMapping("/register")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public LockerRegisterResponseDto register(@RequestBody LockerRegisterDto dto) {
        Locker locker = lockerService.register(dto.getMacAddress());

        return new LockerRegisterResponseDto(
                locker.getId(),
                locker.getSkiResort().getId(),
                locker.getSkiResort().getTenantId()
        );
    }

    @GetMapping("/mac/{mac}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public LockerDto getByMac(@PathVariable String mac) {
        return lockerMapper.toDto(lockerService.getByMacAddress(mac));
    }

    @GetMapping("/{id}/locks")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public Page<LockDto> getLocks(@PathVariable Integer id, Pageable pageable) {
        return lockService.getAllByLockerId(id, pageable).map(lockMapper::toDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        lockerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}