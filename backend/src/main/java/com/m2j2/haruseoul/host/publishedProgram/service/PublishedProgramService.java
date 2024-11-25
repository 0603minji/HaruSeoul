package com.m2j2.haruseoul.host.publishedProgram.service;

import com.m2j2.haruseoul.entity.PublishedProgram;
import com.m2j2.haruseoul.host.publishedProgram.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface PublishedProgramService {
    PublishedProgramCreatedDto create(PublishedProgramCreateDto publishedProgramCreateDto);
    PublishedProgramUpdatedDto update(PublishedProgramUpdateDto publishedProgramUpdateDto);
    PublishedProgramResponseDto getList(List<Long> memberIds, List<LocalDate> dates, List<Long> statusIds, List<Long> programIds,
                                        Integer page, Integer pageSize, String sortBy, String order);
    // controller에서 쿼리스트링 unpaged=true일 때
    PublishedProgramResponseDto getList(List<Long> memberIds, List<LocalDate> dates, List<Long> statusIds, List<Long> programIds,
                                        String sortBy, String order);
    PublishedProgramResponseDto getList(List<Long> memberIds, List<LocalDate> dates, List<Long> statusIds, List<Long> programIds,
                                        Integer page, Integer pageSize, String sortBy, String order, String tab);
    boolean delete(Long id);
    PublishedProgramProgramFilterResponseDto getProgramFilterList(Long hostId);
    OnGoingPublishedProgramListDto findByProgramId(Long pId);
    PublishedProgramListDto getById(Long id);
}