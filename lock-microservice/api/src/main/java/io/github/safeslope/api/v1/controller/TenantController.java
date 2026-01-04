package io.github.safeslope.api.v1.controller;

import io.github.safeslope.api.v1.dto.SkiResortDto;
import io.github.safeslope.api.v1.mapper.SkiResortMapper;
import io.github.safeslope.skiresort.service.SkiResortService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {

    private final SkiResortService skiResortService;
    private final SkiResortMapper skiResortMapper;

    public TenantController(SkiResortService skiResortService, SkiResortMapper skiResortMapper) {
        this.skiResortService = skiResortService;
        this.skiResortMapper = skiResortMapper;
    }

    @GetMapping("/{id}/ski-resorts")
    public List<SkiResortDto> getSkiResorts(@PathVariable String id) {
        return skiResortMapper.toDtoList(skiResortService.getByTenantId(id));
    }
}
