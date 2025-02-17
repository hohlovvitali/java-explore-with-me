package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService service;


    @GetMapping("/stats")
    public List<ViewStatsDto> get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                  @RequestParam(defaultValue = "") List<String> uris,
                                                  @RequestParam(defaultValue = "false") Boolean unique) {

        log.info("Получен запрос GET /stats. Параметры: start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        return service.getStats(start, end, uris, unique);
    }


    @PostMapping("/hit")
    public void save(@RequestBody EndpointHitDto dto) {
        log.info("Получен запрос POST /hit");
        service.save(dto);
    }
}
