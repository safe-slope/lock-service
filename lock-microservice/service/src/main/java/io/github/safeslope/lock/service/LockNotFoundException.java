package io.github.safeslope.lock.service;

public class LockNotFoundException extends RuntimeException {

    public LockNotFoundException(Integer id) {
        super("Lock with ID " + id + " not found.");
    }

    public LockNotFoundException(String mac) {
        super("Lock with MAC address " + mac + " not found.");
    }
}
