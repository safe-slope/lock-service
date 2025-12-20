package io.github.safeslope.api.v1.controller;

import io.github.safeslope.lock.service.LockCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/locks")
public class LockCommandController {

  private final LockCommandService lockCommandService;

  public LockCommandController(LockCommandService lockCommandService) {
    this.lockCommandService = lockCommandService;
  }

  @PostMapping("/{id}/commands/unlock")
  public ResponseEntity<Void> unlock(@PathVariable Integer id) {
    lockCommandService.unlockByLockId(id);
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/{id}/commands/lock")
  public ResponseEntity<Void> lock(@PathVariable Integer id) {
    lockCommandService.lockByLockId(id);
    return ResponseEntity.accepted().build();
  }
}