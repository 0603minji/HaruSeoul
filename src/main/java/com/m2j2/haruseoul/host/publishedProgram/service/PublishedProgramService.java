package com.m2j2.haruseoul.host.publishedProgram.service;

import com.m2j2.haruseoul.host.publishedProgram.dto.PublishedProgramCreateDto;
import com.m2j2.haruseoul.host.publishedProgram.dto.PublishedProgramCreatedDto;

public interface PublishedProgramService {
    PublishedProgramCreatedDto create(PublishedProgramCreateDto publishedProgramCreateDto);
}
