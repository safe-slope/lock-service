package io.github.safeslope.location.service;

import io.github.safeslope.entities.Location;
import io.github.safeslope.location.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Location get(Integer id) {
        return locationRepository.findById(id)
            .orElseThrow(() -> new LocationNotFoundException(id));
    }

    public Location create(Location location) {
        return locationRepository.save(location);
    }

    public Location update(Integer id, Location location) {
        Location exist = locationRepository.findById(id)
            .orElseThrow(() -> new LocationNotFoundException(id));
        location.setId(exist.getId());
        return locationRepository.save(location);
    }


    public void delete(Integer id) {
        if (!locationRepository.existsById(id)) {
            throw new LocationNotFoundException(id);
        }
        locationRepository.deleteById(id);
    }
}