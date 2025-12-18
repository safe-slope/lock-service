package io.github.safeslope.lock.repository;

import io.github.safeslope.entities.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LockRepository extends JpaRepository<Lock, Integer> {
    Optional<Lock> findByMacAddress(String macAddress);
    List<Lock> findByLocker_Id(Integer lockerId);
}
