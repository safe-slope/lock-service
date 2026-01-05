package io.github.safeslope.skiresort.repository;

import io.github.safeslope.entities.SkiResort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkiResortRepository extends JpaRepository<SkiResort, Integer> {
     SkiResort findByName(String name);

     List<SkiResort> findSkiResortsByTenantId(Integer tenantId);
}
