package io.github.safeslope.api.v1.controller;


import io.github.safeslope.api.v1.dto.LockDto;
import io.github.safeslope.api.v1.dto.LockerDto;
import io.github.safeslope.api.v1.dto.SkiResortDto;
import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.skiresort.service.SkiResortService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/skiresorts")
public class SkiResortController {

    private final SkiResortService skiResortService;

    public SkiResortController(SkiResortService skiResortService) {
        this.skiResortService = skiResortService;
    }

    @GetMapping
    public List<SkiResortDto> list() {
        return skiResortService.getAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SkiResortDto get(@PathVariable Integer id) { return toDto(skiResortService.get(id));}

    @GetMapping("/name/{name}")
    public SkiResortDto getByName(@PathVariable String name) { return toDto(skiResortService.getByName(name));}

    @GetMapping("/{id}/lockers")
    public List<LockerDto> getLockers(@PathVariable Integer id) {
        // TODO klici metodo v SkiResortService
    }

    @GetMapping("/{id}/locks")
    public List<LockDto> getLocks(@PathVariable Integer id) {
        // TODO klici metodo v SkiResortService
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        skiResortService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<SkiResortDto> create(@RequestBody SkiResortDto dto) {
        SkiResort entity = fromDto(dto);
        SkiResort saved = skiResortService.create(entity);
        return ResponseEntity.ok(toDto(saved));
    }


    private SkiResortDto toDto(SkiResort s) {
        return new SkiResortDto(
                s.getId(),
                s.getName(),
                s.getAddress()
        );
    }

    private SkiResort fromDto(SkiResortDto d){
        return SkiResort.builder()
                .name(d.getName())
                .address(d.getAddress())
                .build();
    }

}
