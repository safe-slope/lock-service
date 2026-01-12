package io.github.safeslope.lock.repository;

import io.github.safeslope.entities.Lock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LockRepository extends JpaRepository<Lock, Integer> {
    Lock findByMacAddress(String macAddress);
    List<Lock> findByLocker_Id(Integer lockerId);
    Page<Lock> findByLocker_Id(Integer lockerId, Pageable pageable);
    List<Lock> findByLocker_SkiResort_Id(Integer skiResortId);
    Page<Lock> findByLocker_SkiResort_Id(Integer skiResortId, Pageable pageable);
}
