package com.m2j2.haruseoul.anonymous.program.service;

import com.m2j2.haruseoul.anonymous.program.dto.ProgramDetailReviewDto;

public interface ProgramDetailReviewService {
    ProgramDetailReviewDto getList(Long programId);
}
