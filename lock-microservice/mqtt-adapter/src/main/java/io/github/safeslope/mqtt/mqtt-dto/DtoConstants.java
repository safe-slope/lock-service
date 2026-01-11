package io.github.safeslope.mqtt.dto;

public class DtoConstants {

    public enum Command {
        LOCK,
        UNLOCK,
        SET_MODE_TO_NORMAL,
        SET_MODE_TO_SERVICE,
        SET_MODE_TO_MAINTENANCE,
        SET_MODE_TO_DISABLED,
    }

    public enum Status {
        SUCCESS, FAILURE
    }
}