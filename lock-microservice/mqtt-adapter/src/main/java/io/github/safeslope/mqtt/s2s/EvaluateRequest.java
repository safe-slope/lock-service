package io.github.safeslope.mqtt.s2s;

public record EvaluateRequest(
        Integer skiTicketId,
        Integer lockId,
        Integer lockerId,
        Integer resortId,
        AntiAbuseAction action,
        Long timestamp
) {}
