package io.github.safeslope.lockevent.service;

public class LockEventNotFoundException extends RuntimeException {

    public LockEventNotFoundException(Integer id) {
        super("LockEvent with ID " + id + " not found.");
    }
}
