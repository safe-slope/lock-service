package io.github.safeslope.skiticket.service;

public class SkiTicketNotFoundException extends RuntimeException {
    public SkiTicketNotFoundException(Integer id) {
        super("SkiTicket with ID " + id + " not found.");
    }
}
