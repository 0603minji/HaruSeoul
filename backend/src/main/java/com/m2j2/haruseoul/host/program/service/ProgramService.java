package com.m2j2.haruseoul.host.program.service;

import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.host.program.dto.ProgramCreateDto;
import com.m2j2.haruseoul.host.program.dto.ProgramFilterListDto;
import com.m2j2.haruseoul.host.program.dto.ProgramResponseDto;
import com.m2j2.haruseoul.host.program.dto.ProgramUpdateDto;

import java.util.List;

public interface ProgramService {
    ProgramResponseDto getList(Long id, List<Long> programIds, List<Long> categoryIds,
                               List<String> statuses, int pageNum, int cardsPerPage);

    List<ProgramFilterListDto> getList(Long hostId, List<Long>pIds, List<String> statuses);

    Program create(ProgramCreateDto programCreateDto);

    Program update(ProgramUpdateDto programUpdateDto);

    void delete(Long programId);
}
