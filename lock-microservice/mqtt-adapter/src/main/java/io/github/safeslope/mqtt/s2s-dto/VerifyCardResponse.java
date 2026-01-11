package io.github.safeslope.mqtt.s2s.dto;


public record VerifyCardResponse(boolean valid, String status, Integer skiTicketId, String message) {}
