package com.m2j2.haruseoul.host.program.service;

import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.host.program.dto.ProgramCreateDto;
import com.m2j2.haruseoul.host.program.dto.ProgramResponseDto;

import java.util.List;

public interface ProgramService {
    ProgramResponseDto getList(List<Long> programIds, List<Long> categoryIds,
                               List<String> statuses);

    Program create(ProgramCreateDto programCreateDto);
}