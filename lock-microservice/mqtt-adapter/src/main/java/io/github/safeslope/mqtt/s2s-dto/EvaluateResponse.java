package io.github.safeslope.mqtt.s2s.dto;

public record EvaluateResponse(
        AntiAbuseDecision decision,
        AntiAbuseCardState cardState,
        String reason,
        Long blockedUntil
) {}
