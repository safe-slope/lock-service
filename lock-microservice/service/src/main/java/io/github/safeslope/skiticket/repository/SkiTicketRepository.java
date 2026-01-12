package io.github.safeslope.skiticket.repository;

import io.github.safeslope.entities.SkiTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkiTicketRepository extends JpaRepository<SkiTicket, Integer> {
    List<SkiTicket> findBySkiResort_Id(Integer skiResortId);
    Page<SkiTicket> findBySkiResort_Id(Integer skiResortId, Pageable pageable);
}
