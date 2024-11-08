package com.m2j2.haruseoul.host.program.service;

import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.host.program.dto.ProgramCreateDto;
import com.m2j2.haruseoul.host.program.dto.ProgramFilterDto;
import com.m2j2.haruseoul.host.program.dto.ProgramResponseDto;
import com.m2j2.haruseoul.host.program.dto.ProgramUpdateDto;

import java.util.List;

public interface ProgramService {
    ProgramResponseDto getList(List<Long> programIds, List<Long> categoryIds,
                               List<String> statuses, int pageNum, int cardsPerPage);

    List<ProgramFilterDto> getList(Long hostId, List<String> statuses);

    Program create(ProgramCreateDto programCreateDto);

    Program update(ProgramUpdateDto programUpdateDto);

    void delete(Long programId);
}
