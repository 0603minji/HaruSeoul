package com.m2j2.haruseoul.anonymous.program.service;


import com.m2j2.haruseoul.anonymous.program.dto.ProgramDetailRouteDto;
import com.m2j2.haruseoul.entity.Route;
import com.m2j2.haruseoul.repository.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultProgramDetailRouteService implements ProgramDetailRouteService {

    private final RouteRepository routeRepository;

    public DefaultProgramDetailRouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    @Transactional
    public List<ProgramDetailRouteDto> getRoutes(Long id) {
        List<Route> routes = routeRepository.findByProgramId(id);

        return routes.stream()
                .map(route -> {
                    LocalTime duration = route.getDuration();
                    Integer durationHour = duration != null ? duration.getHour() : 0;
                    Integer durationMinute = duration != null ? duration.getMinute() : 0;

                    String transportationName = route.getTransportation() != null
                            ? route.getTransportation().getName()
                            : "이동수단 없음";



                    return ProgramDetailRouteDto.builder()
                            .startTime(route.getStartTime())
                            .title(route.getTitle())
                            .durationHour(durationHour)
                            .durationMinute(durationMinute)
                            .order(route.getOrder())
                            .transportation(transportationName)
                            .transportTime(route.getTransportationDuration())
                            .description(route.getDescription())
                            .address(route.getAddress())
                            .build();
                })
                .toList();
    }



}

