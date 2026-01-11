package io.github.safeslope.mqtt.s2s;

public record EvaluateResponse(
        AntiAbuseDecision decision,
        AntiAbuseCardState cardState,
        String reason,
        Long blockedUntil
) {}
