package com.m2j2.haruseoul.anonymous.program.service;

import com.m2j2.haruseoul.anonymous.program.dto.ProgramDetailProgramDto;

public interface ProgramDetailService {
    ProgramDetailProgramDto getDetail(Long programId);
}
