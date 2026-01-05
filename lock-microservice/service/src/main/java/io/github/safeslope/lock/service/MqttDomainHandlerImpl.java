package io.github.safeslope.lock.service;

import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.LockEvent;
import io.github.safeslope.entities.LockEvent.EventType;
import io.github.safeslope.lockevent.service.LockEventService;
import io.github.safeslope.mqtt.MqttDomainHandler;
import io.github.safeslope.mqtt.dto.MqttLockEventDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MqttDomainHandlerImpl implements MqttDomainHandler {

    private final LockService lockService;
    private final LockEventService lockEventService;

    public MqttDomainHandlerImpl(LockService lockService, LockEventService lockEventService) {
        this.lockService = lockService;
        this.lockEventService = lockEventService;
    }

    @Override
    public void onLockEvent(String tenantId, String lockKey, MqttLockEventDto dto) {
        Lock lock = lockService.getByMacAddress(lockKey);

        LockEvent event = LockEvent.builder()
                .lock(lock)
                .eventType(parseEventType(dto.getType()))
                .eventTime(parseTsOrNow(dto.getTs()))
                .build();

        lockEventService.create(event);
    }

    private EventType parseEventType(String type) {
        return EventType.valueOf(type.trim().toUpperCase());
    }

    private LocalDateTime parseTsOrNow(String ts) {
        if (ts == null || ts.isBlank()) return LocalDateTime.now();
        try { return LocalDateTime.parse(ts); } catch (Exception ignored) {}
        return LocalDateTime.now();
    }
}
