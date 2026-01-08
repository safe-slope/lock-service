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
import org.springframework.http.ResponseEntity;
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
    public List<LockerDto> list() {
        return lockerService.getAll().stream().map(lockerMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LockerDto get(@PathVariable Integer id) {
        return lockerMapper.toDto(lockerService.get(id));
    }

    @PutMapping("/{id}")
    public LockerDto update(@PathVariable Integer id, @RequestBody LockerDto dto) {
        return lockerMapper.toDto(lockerService.update(id, lockerMapper.toEntity(dto)));
    }

    @GetMapping("/unassgined")
    public List<LockerDto> getUnassigned() {
        return lockerMapper.toDtoList(lockerService.getAllUnassigned());
    }

    @GetMapping("/register")
    public LockerRegisterResponseDto register(@RequestBody LockerRegisterDto dto) {
        Locker locker = lockerService.register(dto.getMacAddress());

        return new LockerRegisterResponseDto(
                locker.getId(),
                locker.getSkiResort().getId(),
                locker.getSkiResort().getTenantId()
        );
    }

    @GetMapping("/mac/{mac}")
    public LockerDto getByMac(@PathVariable String mac) {
        return lockerMapper.toDto(lockerService.getByMacAddress(mac));
    }

    @GetMapping("/{id}/locks")
    public List<LockDto> getLocks(@PathVariable Integer id) {
        return lockMapper.toDtoList(lockService.getAllByLockerId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        lockerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}