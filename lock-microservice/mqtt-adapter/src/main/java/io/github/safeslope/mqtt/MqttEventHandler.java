package io.github.safeslope.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.safeslope.mqtt.dto.MqttLockEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MqttEventHandler {

    private static final Logger log = LoggerFactory.getLogger(MqttEventHandler.class);

    private final ObjectMapper objectMapper;
    private final MqttDomainHandler domainHandler;

    public MqttEventHandler(ObjectMapper objectMapper, MqttDomainHandler domainHandler) {
        this.objectMapper = objectMapper;
        this.domainHandler = domainHandler;
    }

    public void handleLockEventMessage(String topic, String payload) {
        try {
            TopicParts parts = parseTopic(topic);

            MqttLockEventDto dto = objectMapper.readValue(payload, MqttLockEventDto.class);

            domainHandler.onLockEvent(parts.tenantId(), parts.lockKey(), dto);

        } catch (Exception e) {
            log.error("Failed handling MQTT event topic={} payload={}", topic, payload, e);
        }
    }

    private TopicParts parseTopic(String topic) {
        String[] p = topic.split("/");
        if (p.length < 5) throw new IllegalArgumentException("Unexpected topic: " + topic);
        return new TopicParts(p[1], p[3]);
    }

    private record TopicParts(String tenantId, String lockKey) {}
}
