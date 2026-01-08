package io.github.safeslope.mqtt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.time. Instant;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MqttMessage {

    private MessageType messageType;
    private String messageId;
    private Instant timestamp;
    private String lockerId;
    private Map<String, Object> payload;


    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }


    public enum MessageType {
        REQUEST,
        COMMAND,
        STATUS,
        EVENT
    }

    //REQUEST BUILDERS

    public static MqttMessage createUnlockRequest(String lockerId, String lockId, String ticketId) {
        MqttMessage message = new MqttMessage();
        message.messageType = MessageType.REQUEST;
        message.messageId = UUID.randomUUID().toString();
        message.timestamp = Instant. now();
        message.lockerId = lockerId;
        message. payload = Map.of(
                "target", "LOCK",
                "lockId", lockId,
                "action", "UNLOCK",
                "source", "SKI_TICKET",
                "ticketId", ticketId
        );
        return message;
    }

    public static MqttMessage createLockRequest(String lockerId, String lockId) {
        MqttMessage message = new MqttMessage();
        message.messageType = MessageType.REQUEST;
        message.messageId = UUID.randomUUID().toString();
        message.timestamp = Instant.now();
        message.lockerId = lockerId;
        message.payload = Map.of(
                "target", "LOCK",
                "lockId", lockId,
                "action", "LOCK",
                "source", "AUTO_CLOSE"
        );
        return message;
    }

    //COMMAND BUILDERS

    public static MqttMessage createUnlockCommand(String lockerId, String lockId, String requestId) {
        MqttMessage message = new MqttMessage();
        message.messageType = MessageType.COMMAND;
        message.messageId = UUID.randomUUID().toString();
        message.timestamp = Instant.now();
        message.lockerId = lockerId;
        message.payload = Map.of(
                "requestId", requestId,
                "target", "LOCK",
                "lockId", lockId,
                "command", "UNLOCK"
        );
        return message;
    }

    public static MqttMessage createLockCommand(String lockerId, String lockId, String requestId) {
        MqttMessage message = new MqttMessage();
        message.messageType = MessageType.COMMAND;
        message.messageId = UUID.randomUUID().toString();
        message.timestamp = Instant.now();
        message.lockerId = lockerId;
        message.payload = Map.of(
                "requestId", requestId,
                "target", "LOCK",
                "lockId", lockId,
                "command", "LOCK"
        );
        return message;
    }

    public static MqttMessage createDenyCommand(String lockerId, String lockId, String requestId, String reason) {
        MqttMessage message = new MqttMessage();
        message.messageType = MessageType.COMMAND;
        message.messageId = UUID.randomUUID().toString();
        message.timestamp = Instant.now();
        message.lockerId = lockerId;
        message.payload = Map.of(
                "requestId", requestId,
                "target", "LOCK",
                "lockId", lockId,
                "command", "DENY",
                "reason", reason
        );
        return message;
    }

    public static MqttMessage createSetModeCommand(String lockerId, String lockId, String mode) {
        MqttMessage message = new MqttMessage();
        message.messageType = MessageType.COMMAND;
        message.messageId = UUID.randomUUID().toString();
        message.timestamp = Instant.now();
        message.lockerId = lockerId;
        message.payload = Map.of(
                "target", "LOCK",
                "lockId", lockId,
                "command", "SET_MODE",
                "mode", mode
        );
        return message;
    }

    //STATUS BUILDERS

    public static MqttMessage createModeStatus(String lockerId, String lockId, String mode) {
        MqttMessage message = new MqttMessage();
        message.messageType = MessageType.STATUS;
        message.messageId = UUID.randomUUID().toString();
        message.timestamp = Instant.now();
        message.lockerId = lockerId;
        message.payload = Map.of(
                "lockId", lockId,
                "mode", mode
        );
        return message;
    }

    public static MqttMessage createStateStatus(String lockerId, String lockId, String state) {
        MqttMessage message = new MqttMessage();
        message.messageType = MessageType. STATUS;
        message.messageId = UUID.randomUUID().toString();
        message.timestamp = Instant.now();
        message.lockerId = lockerId;
        message.payload = Map.of(
                "lockId", lockId,
                "state", state
        );
        return message;
    }

    //EVENT BUILDERS

    public static MqttMessage createEvent(String lockerId, String lockId, String event, String reason) {
        MqttMessage message = new MqttMessage();
        message.messageType = MessageType.EVENT;
        message.messageId = UUID.randomUUID().toString();
        message.timestamp = Instant.now();
        message.lockerId = lockerId;
        message.payload = Map.of(
                "lockId", lockId,
                "event", event,
                "reason", reason
        );
        return message;
    }
}