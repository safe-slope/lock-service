package io.github.safeslope.skiresort.service;

import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.skiresort.repository.SkiResortRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SkiResortService {

    private final SkiResortRepository skiResortRepository;

    public SkiResortService(SkiResortRepository skiResortRepository) {
        this.skiResortRepository = skiResortRepository;
    }

    public List<SkiResort> getAll() {
        return skiResortRepository.findAll();
    }

    public Page<SkiResort> getAll(Pageable pageable) {
        return skiResortRepository.findAll(pageable);
    }
    public SkiResort get(Integer id) {
        return skiResortRepository.findById(id).
                orElseThrow(() -> new SkiResortNotFoundException(id));
    }

    public SkiResort getByName(String name) {
        SkiResort skiResort = skiResortRepository.findByName(name);

        if(skiResort == null){
            throw new SkiResortNotFoundException(name);
        }
        return skiResort;
    }

    public List<SkiResort> getByTenantId(Integer tenantId) {
        return skiResortRepository.findSkiResortsByTenantId(tenantId);
    }

    public SkiResort create(SkiResort resort) {
        return skiResortRepository.save(resort);
    }

    public SkiResort update(Integer id, SkiResort updated) {
        SkiResort existing = skiResortRepository.findById(id)
            .orElseThrow(() -> new SkiResortNotFoundException(id));

        existing.setTenantId(updated.getTenantId());
        existing.setName(updated.getName());
        existing.setAddress(updated.getAddress());
        return skiResortRepository.save(existing);
    }

    public void delete(Integer id) {
        if (!skiResortRepository.existsById(id)) {
            throw new SkiResortNotFoundException(id);
        }
        skiResortRepository.deleteById(id);
    }
}
