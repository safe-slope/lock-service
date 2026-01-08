package io.github.safeslope.locker.service;

import io.github.safeslope.entities.Locker;
import io.github.safeslope.locker.repository.LockerRepository;
import io.github.safeslope.skiresort.repository.SkiResortRepository;
import io.github.safeslope.skiresort.service.SkiResortNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LockerService{

    private final LockerRepository lockerRepository;
    private final SkiResortRepository skiResortRepository;

    public LockerService(LockerRepository lockerRepository, SkiResortRepository skiResortRepository) {
        this.lockerRepository = lockerRepository;
        this.skiResortRepository = skiResortRepository;
    }

    public List<Locker> getAll() {
        return lockerRepository.findAll();
    }

    public Locker get(Integer id) {
        return lockerRepository.findById(id)
            .orElseThrow(() -> new LockerNotFoundException(id));
    }

    public List<Locker> getAllUnassigned() {
        //returns all lockers without ski resort specified
        return lockerRepository.findBySkiResortIsNull();
    }

    public Locker getByMacAddress(String mac) {
        Locker locker = lockerRepository.findByMacAddress(mac);

        if (locker == null) {
            throw new LockerNotFoundException(mac);
        }
        return locker;
    }

    public List<Locker> getAllBySkiResortId(Integer skiResortId) {
        if (!skiResortRepository.existsById(skiResortId)) {
            throw new SkiResortNotFoundException(skiResortId);
        }
        return lockerRepository.findBySkiResort_Id(skiResortId);
    }

    public Locker create(Locker locker) {
        return lockerRepository.save(locker);
    }

    public Locker update(Integer id, Locker locker) {
        lockerRepository.findById(id)
            .orElseThrow(() -> new LockerNotFoundException(id));
        locker.setId(id);
        return lockerRepository.save(locker);
    }

    public Locker register(String mac) {
        Locker locker = lockerRepository.findByMacAddress(mac);

        //first check if the locker with specified mac address exists
        if (locker != null) {
            // return the existing locker
            return locker;
        }
        //else create a locker with the ski resort non specified
        else {
            Locker l = Locker.builder()
                    .macAddress(mac)
                    .build();

            return lockerRepository.save(l);
        }
    }

    public void delete(Integer id) {
        if (!lockerRepository.existsById(id)) {
            throw new LockerNotFoundException(id);
        }
        lockerRepository.deleteById(id);
    }
}