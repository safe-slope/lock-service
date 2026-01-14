package io.github.safeslope.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            io.github.safeslope.lock.service.LockNotFoundException.class,
            io.github.safeslope.locker.service.LockerNotFoundException.class,
            io.github.safeslope.location.service.LocationNotFoundException.class,
            io.github.safeslope.skiresort.service.SkiResortNotFoundException.class,
            io.github.safeslope.skiticket.service.SkiTicketNotFoundException.class,
            io.github.safeslope.lockevent.service.LockEventNotFoundException.class
    })
    public ResponseEntity<String> handleNotFound(RuntimeException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500).body("An internal error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500).body("An unexpected error occurred");
    }
}