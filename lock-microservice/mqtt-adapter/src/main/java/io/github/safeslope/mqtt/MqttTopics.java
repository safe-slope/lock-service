package io.github.safeslope.mqtt;

public final class MqttTopics {

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
}

