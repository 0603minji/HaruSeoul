package com.m2j2.haruseoul.anonymous.program.service;

import com.m2j2.haruseoul.anonymous.program.dto.ProgramDetailRouteDto;

import java.util.List;

public interface ProgramDetailRouteService {
    List<ProgramDetailRouteDto> getRoutes(Long id);

}