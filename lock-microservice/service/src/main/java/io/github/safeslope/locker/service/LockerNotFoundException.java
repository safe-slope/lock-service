package io.github.safeslope.locker.service;

public class LockerNotFoundException extends RuntimeException {

    public LockerNotFoundException(Integer id) {
        super("Locker with ID " + id + " not found.");
    }

    public LockerNotFoundException(String mac) {
        super("Locker with MAC address " + mac + " not found.");
    }
}
