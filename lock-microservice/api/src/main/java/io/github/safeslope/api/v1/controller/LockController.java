package io.github.safeslope.api.v1.controller;

import io.github.safeslope.api.v1.dto.LockDto;
import io.github.safeslope.api.v1.mapper.LockMapper;
import io.github.safeslope.lock.service.LockService;
import io.github.safeslope.mqtt.service.MqttLockService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/locks")
public class LockController {

    private final LockService lockService;
    private final LockMapper lockMapper;
    private final MqttLockService mqttLockService;

    public LockController(LockService lockService, LockMapper lockMapper, MqttLockService mqttLockService) {
        this.lockService = lockService;
        this.lockMapper = lockMapper;
        this.mqttLockService = mqttLockService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public Page<LockDto> list(Pageable pageable) {
        return lockService.getAllLocks(pageable).map(lockMapper::toDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public LockDto get(@PathVariable Integer id) {
        return lockMapper.toDto(lockService.getLock(id));
    }

    @GetMapping("/mac/{mac}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public LockDto getByMac(@PathVariable String mac) {
        return lockMapper.toDto(lockService.getByMacAddress(mac));
    }

    @PostMapping("/{mac}/unlock")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<Void> unlock(@PathVariable String mac) {
        mqttLockService.unlock(mac);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{mac}/lock")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<Void> lock(@PathVariable String mac) {
        mqttLockService.lock(mac);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        lockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}