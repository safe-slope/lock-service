package io.github.safeslope.mqtt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public final class MqttTopics {
    private final String requestTopic = "tenant/+/resort/+/locker/+/request";
    private final String statusTopic =  "tenant/+/resort/+/locker/+/status";
    private final String registrationTopic = "tenant/+/resort/+/locker/+/register";

    @AllArgsConstructor
    public static class TopicProperties {
        Integer tenantId;
        Integer resortId;
        Integer lockerId;
        String type;
    }

    public String lockerCommand(Integer tenantId, Integer resortId, Integer lockerId) {
        return String.format(
                "tenant/%s/resort/%s/locker/%s/command",
                tenantId, resortId, lockerId
        );
    }

    public String lockerRequest(Integer tenantId, Integer resortId, Integer lockerId) {
        return String.format(
                "tenant/%s/resort/%s/locker/%s/request",
                tenantId, resortId, lockerId
        );
    }

    public String lockerStatus(Integer tenantId, Integer resortId, Integer lockerId) {
        return String.format(
                "tenant/%s/resort/%s/locker/%s/status",
                tenantId, resortId, lockerId
        );
    }

    //registers locks, not lockers!
    public String lockerRegistration(Integer tenantId, Integer resortId, Integer lockerId) {
        return String.format(
                "tenant/%s/resort/%s/locker/%s/register",
                tenantId, resortId, lockerId
        );
    }

    public String lockerResponse(Integer tenantId, Integer resortId, Integer lockerId) {
        return String.format(
                "tenant/%s/resort/%s/locker/%s/response",
                tenantId, resortId, lockerId
        );
    }

    public static TopicProperties tokenizeTopic(String topic) {
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("Topic cannot be null or empty");
        }

        String[] parts = topic.split("/");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid topic format: expected 'tenant/{id}/resort/{id}/locker/{id}/{type}'");
        }

        if (!"tenant".equals(parts[0]) || !"resort".equals(parts[2]) || !"locker".equals(parts[4])) {
            throw new IllegalArgumentException("Invalid topic structure");
        }

        try {
            Integer tenantId = Integer.parseInt(parts[1]);
            Integer resortId = Integer.parseInt(parts[3]);
            Integer lockerId = Integer.parseInt(parts[5]);
            String type = parts[6];

            return new TopicProperties(tenantId, resortId, lockerId, type);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID format in topic", e);
        }
    }

}

