package com.m2j2.haruseoul.anonymous.program.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramDetailResponseDto {

    private ProgramDetailProgramDto programDetailProgramDto;
    private ProgramDetailCategoryDto programDetailCategoryDto;
    private ProgramDetailImageDto programDetailImageDto;
    private ProgramDetailMemberDto programDetailMemberDto;
    private ProgramDetailReviewDto programDetailReviewDto;
    private List<ProgramDetailRouteDto> programDetailRouteDto;
}
