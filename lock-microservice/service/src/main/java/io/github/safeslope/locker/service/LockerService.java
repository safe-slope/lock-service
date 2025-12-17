package io.github.safeslope.locker.service;

import io.github.safeslope.entities.Lock;
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

    public List<Lock> getLocks(Integer lockerId) {
        // TODO implementiraj metodo, ki najde vse Locke za Locker s podanim id-jem
    }

    public Locker create(Locker locker) {
        return lockerRepository.save(locker);
    }

    public Locker update(Locker locker) {
        // optional: verify exists first, or rely on save() to upsert behavior
        if (locker.getId() == null || !lockerRepository.existsById(locker.getId())) {
            throw new LockerNotFoundException(locker.getId());
        }
        return lockerRepository.save(locker);
    }

    public void delete(Integer id) {
        lockerRepository.deleteById(id);
    }
}