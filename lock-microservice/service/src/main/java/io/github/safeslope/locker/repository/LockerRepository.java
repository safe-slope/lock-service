package io.github.safeslope.locker.repository;

import io.github.safeslope.entities.Locker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LockerRepository extends JpaRepository<Locker, Integer> {
    Locker findByMacAddress(String macAddress);
    List<Locker> findBySkiResort_Id(Integer skiResortId);
    Page<Locker> findBySkiResort_Id(Integer skiResortId, Pageable pageable);
    List<Locker> findBySkiResortIsNull();
    Page<Locker> findBySkiResortIsNull(Pageable pageable);
}
