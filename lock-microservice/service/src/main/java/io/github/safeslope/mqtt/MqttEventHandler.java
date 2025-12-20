package io.github.safeslope.service.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.safeslope.entities.Lock;
import io.github.safeslope.entities.LockEvent;
import io.github.safeslope.entities.LockEvent.EventType;
import io.github.safeslope.lock.service.LockService;
import io.github.safeslope.lockevent.service.LockEventService;
import io.github.safeslope.service.mqtt.dto.MqttLockEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class MqttEventHandler {

    private static final Logger log = LoggerFactory.getLogger(MqttEventHandler.class);

    private final ObjectMapper objectMapper;
    private final LockService lockService;
    private final LockEventService lockEventService;

    public MqttEventHandler(ObjectMapper objectMapper,
                            LockService lockService,
                            LockEventService lockEventService) {
        this.objectMapper = objectMapper;
        this.lockService = lockService;
        this.lockEventService = lockEventService;
    }

    public void handleLockEventMessage(String topic, String payload) {
        try {
            TopicParts parts = parseTopic(topic); // tenantId + lockKey

            MqttLockEventDto dto = objectMapper.readValue(payload, MqttLockEventDto.class);

            // tenant aware
           // Lock lock = lockService.getByTenantAndMac(parts.tenantId(), parts.lockKey());
            
           
            Lock lock = lockService.getByMacAddress(parts.lockKey());

            LockEvent event = new LockEvent();
            event.setLock(lock);
            event.setEventType(parseEventType(dto.type()));
            event.setEventTime(parseTsOrNow(dto.ts()));

            lockEventService.create(event);

        } catch (Exception e) {
            log.error("Failed handling MQTT event topic={} payload={}", topic, payload, e);
        }
    }

    // Expected: tenants/{tenantId}/locks/{lockKey}/events
    private TopicParts parseTopic(String topic) {
        String[] p = topic.split("/");
        String tenantId = p[1];
        String lockKey = p[3];
      
        return new TopicParts(tenantId, lockKey);
    }

    private EventType parseEventType(String type) {
        return EventType.valueOf(type.trim().toUpperCase());
    }

    private LocalDateTime parseTsOrNow(String ts) {
        if (ts == null || ts.isBlank()) return LocalDateTime.now();
        try { return LocalDateTime.parse(ts); } catch (Exception ignored) {}
        return LocalDateTime.now();
    }

    private record TopicParts(String tenantId, String lockKey) {}
}
