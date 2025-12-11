package io.github.safeslope.skiresort.repository;

import io.github.safeslope.entities.SkiResort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkiResortRepository extends JpaRepository<SkiResort, Integer> {
     Optional<SkiResort> findByName(String name);
}
