package io.github.safeslope.api.v1.controller;

import io.github.safeslope.api.v1.dto.LockDto;
import io.github.safeslope.api.v1.mapper.LockMapper;
import io.github.safeslope.entities.Lock;
import io.github.safeslope.lock.service.LockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/locks")
public class LockController {

    private final LockService lockService;
    private final LockMapper lockMapper;

    public LockController(LockService lockService, LockMapper lockMapper) {
        this.lockService = lockService;
        this.lockMapper = lockMapper;
    }

    @GetMapping
    public List<LockDto> list() {
        return lockService.getAllLocks().stream().map(lockMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LockDto get(@PathVariable Integer id) {
        return lockMapper.toDto(lockService.getLock(id));
    }

    @GetMapping("/mac/{mac}")
    public LockDto getByMac(@PathVariable String mac) {
        return lockMapper.toDto(lockService.getByMacAddress(mac));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        lockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}