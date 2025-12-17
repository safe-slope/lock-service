package io.github.safeslope.api.v1.controller;


import io.github.safeslope.api.ApiConstants;
import io.github.safeslope.api.v1.dto.SkiResortDto;
import io.github.safeslope.entities.SkiResort;
import io.github.safeslope.skiresort.service.SkiResortService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiConstants.V1 + "/skiresorts")
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



    private SkiResortDto toDto(SkiResort s) {
        return new SkiResortDto(
                s.getId(),
                s.getName(),
                s.getAddress()
        );
    }
}
