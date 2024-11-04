package com.m2j2.haruseoul.host.publishedProgram.service;

import com.m2j2.haruseoul.entity.PublishedProgram;
import com.m2j2.haruseoul.host.publishedProgram.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface PublishedProgramService {
    PublishedProgramCreatedDto create(PublishedProgramCreateDto publishedProgramCreateDto);
    PublishedProgramUpdatedDto update(PublishedProgramUpdateDto publishedProgramUpdateDto);
    PublishedProgramResponseDto getList(List<Long> memberIds, List<LocalDate> dates, List<Long> statusIds, List<Long> programIds);
    boolean delete(Long id);
}