package ru.practicum.ewm.service;

import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    void save(EndpointHitDto dto);
}
