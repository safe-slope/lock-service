package io.github.safeslope.skiresort.service;

public class SkiResortNotFoundException extends RuntimeException {
    public SkiResortNotFoundException(Integer id) {
        super("Ski resort with ID " + id + " not found.");
    }
}
