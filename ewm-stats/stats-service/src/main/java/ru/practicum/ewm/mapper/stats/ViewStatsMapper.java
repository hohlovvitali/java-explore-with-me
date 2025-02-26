package ru.practicum.ewm.mapper.stats;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.model.stats.ViewStats;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ViewStatsMapper {

    public ViewStats toEntity(ViewStatsDto dto) {
        return ViewStats.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .hits(dto.getHits())
                .build();
    }

    public ViewStatsDto toDto(ViewStats entity) {
        return ViewStatsDto.builder()
                .app(entity.getApp())
                .uri(entity.getUri())
                .hits(entity.getHits())
                .build();
    }

    public static List<ViewStatsDto> toDtoList(List<ViewStats> viewStats) {
        return viewStats.stream().map(ViewStatsMapper::toDto).collect(Collectors.toList());
    }
}
