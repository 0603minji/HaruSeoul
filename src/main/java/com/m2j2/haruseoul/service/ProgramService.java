package com.m2j2.haruseoul.service;

import com.m2j2.haruseoul.dto.ProgramResponseDto;
import com.m2j2.haruseoul.entity.Program;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProgramService {
    ProgramResponseDto getList(List<Long> programIds, List<Long> categoryIds,
                               List<String> statuses);
}
