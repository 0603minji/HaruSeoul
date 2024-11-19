package com.m2j2.haruseoul.anonymous.program.service;

import com.m2j2.haruseoul.anonymous.program.dto.ProgramDetailProgramDto;
import com.m2j2.haruseoul.anonymous.program.dto.ProgramDetailResponseDto;
import com.m2j2.haruseoul.anonymous.program.dto.ProgramResponseDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface ProgramService {
    ProgramResponseDto getList(List<Long> programIds, List<Long> categoryIds,
                               String title, LocalDate startDate, LocalDate endDate,
                               Integer minPrice, Integer maxPrice,
                               Integer groupSizeMax, Integer groupSizeMin,
                               Integer duration, Integer startTime, String language, Integer page, Integer pageSize);



    ProgramDetailResponseDto getList(Long id);
}
