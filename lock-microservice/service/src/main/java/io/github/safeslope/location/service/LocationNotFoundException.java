package io.github.safeslope.location.service;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(Integer id) {
        super("Location with ID " + id + " not found.");
    }
}