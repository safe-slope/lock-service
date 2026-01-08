package io.github.safeslope.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.safeslope.mqtt.dto.MqttMessage;
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

            MqttMessage dto = objectMapper.readValue(payload, MqttMessage.class);

            domainHandler.onLockEvent(parts.tenantId(), parts.resortId(), parts.lockerId(), dto);

        } catch (Exception e) {
            log.error("Failed handling MQTT event topic={} payload={}", topic, payload, e);
        }
    }

    private TopicParts parseTopic(String topic) {
        String[] p = topic.split("/");
        if (p.length != 7) throw new IllegalArgumentException("Unexpected topic: " + topic);
        
        // Topic format: tenant/{tenantId}/resort/{resortId}/locker/{lockerId}/events
        final int TENANT_INDEX = 1;
        final int RESORT_INDEX = 3;
        final int LOCKER_INDEX = 5;
        
        return new TopicParts(p[TENANT_INDEX], p[RESORT_INDEX], p[LOCKER_INDEX]);
    }

    private record TopicParts(String tenantId, String resortId, String lockerId) {}
}
