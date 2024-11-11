package com.m2j2.haruseoul.host.publishedProgram.service;

import com.m2j2.haruseoul.host.publishedProgram.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface PublishedProgramService {
    PublishedProgramCreatedDto create(PublishedProgramCreateDto publishedProgramCreateDto);
    PublishedProgramUpdatedDto update(PublishedProgramUpdateDto publishedProgramUpdateDto);
    PublishedProgramResponseDto getList(List<Long> memberIds, List<LocalDate> dates, List<Long> statusIds, List<Long> programIds,
                                        Integer page, Integer pageSize, String sort, String order);
    PublishedProgramResponseDto getList(List<Long> memberIds, List<LocalDate> dates, List<Long> statusIds, List<Long> programIds,
                                        Integer page, Integer pageSize, String sort, String order, String tab);
    boolean delete(Long id);
}