package io.github.safeslope.locker.repository;

import io.github.safeslope.entities.Locker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LockerRepository extends JpaRepository<Locker, Integer> {
    Optional<Locker> findByMacAddress(String macAddress);
}
