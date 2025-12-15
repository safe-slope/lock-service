package io.github.safeslope.locker.service;

import io.github.safeslope.entities.Locker;
import io.github.safeslope.locker.repository.LockerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LockerService{

    private final LockerRepository lockerRepository;

    public LockerService(LockerRepository lockerRepository) {
        this.lockerRepository = lockerRepository;
    }

    public List<Locker> getAll() {
        return lockerRepository.findAll();
    }

    public Locker get(Integer id) {
        return lockerRepository.findById(id)
            .orElseThrow(() -> new LockerNotFoundException(id));
    }

    public Locker getByMacAddress(String mac) {
        return lockerRepository.findByMacAddress(mac)
            .orElseThrow(() -> new LockerNotFoundException(mac));
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

    public void delete(Integer id) {
        if (!lockerRepository.existsById(id)) {
            throw new LockerNotFoundException(id);
        }
        lockerRepository.deleteById(id);
    }
}