package io.github.safeslope.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            io.github.safeslope.lock.service.LockNotFoundException.class,
            io.github.safeslope.locker.service.LockerNotFoundException.class,
            io.github.safeslope.location.service.LocationNotFoundException.class,
//            io.github.safeslope.skiresort.service.SkiResortNotFoundException.class,
//            io.github.safeslope.skiticket.service.SkiTicketNotFoundException.class,
            io.github.safeslope.lockevent.service.LockEventNotFoundException.class
    })
    public ResponseEntity<String> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}