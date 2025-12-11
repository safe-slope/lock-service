package io.github.safeslope.skiticket.repository;

import io.github.safeslope.entities.SkiTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkiTicketRepository extends JpaRepository<SkiTicket, Integer> {
}
