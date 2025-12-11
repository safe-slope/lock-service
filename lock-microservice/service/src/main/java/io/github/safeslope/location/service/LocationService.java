package io.github.safeslope.location.service;

import io.github.safeslope.entities.Location;
import io.github.safeslope.location.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LocationService{

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location get(Integer id) {
        return locationRepository.findById(id);
    }

    @Override
    public Location create(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location update(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public void delete(Integer id) {
        locationRepository.deleteById(id);
    }
}
