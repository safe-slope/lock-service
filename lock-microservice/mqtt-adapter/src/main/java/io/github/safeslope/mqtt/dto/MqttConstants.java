package io.github.safeslope.mqtt.dto;

public class MqttConstants {

    public enum Action {
        UNLOCK,
        LOCK
    }

    public enum Source {
        SKI_TICKET,
        AUTO_CLOSE,
        MANUAL,
        ADMIN
    }

    public enum Command {
        UNLOCK,
        LOCK,
        DENY,
        SET_MODE
    }

    public enum Mode {
        NORMAL,
        SERVICE,
        MAINTENANCE,
        DISABLED
    }

    public enum State {
        LOCKED,
        UNLOCKED,
        ERROR
    }

    public enum Event {
        COMMAND_FAILED,
        COMMAND_SUCCESS,
        BATTERY_LOW,
        TAMPER_DETECTED
    }

    public enum DenyReason {
        TICKET_EXPIRED,
        TICKET_INVALID,
        LOCK_OCCUPIED,
        LOCK_ERROR
    }

    public enum FailureReason {
        MOTOR_TIMEOUT,
        SENSOR_ERROR,
        COMMUNICATION_ERROR,
        BATTERY_CRITICAL
    }
}