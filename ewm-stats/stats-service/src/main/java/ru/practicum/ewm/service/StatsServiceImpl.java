package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.EndpointHitDto;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.exception.DateTimeException;
import ru.practicum.ewm.mapper.endpoint.EndpointHitMapper;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    public void save(EndpointHitDto endpointHitDto) {
        statsRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris,
                                       boolean unique) {
        if (start == null || end == null) {
            throw new DateTimeException("Необходимо указать время начала и время окончания.");
        }
        if (start.isAfter(end)) {
            throw new DateTimeException("Время окончания раньше времени начала.");
        }
        if (unique && !uris.isEmpty()) {
            return statsRepository.findAllHitsWithUniqueIpWithUris(uris, start, end);
        }
        if (unique) {
            return statsRepository.findAllHitsWithUniqueIpWithoutUris(start, end);
        }
        if (!uris.isEmpty()) {
            return statsRepository.findAllHitsWithUris(uris, start, end);
        }
        return statsRepository.findAllHitsWithoutUris(start, end);
    }
}
