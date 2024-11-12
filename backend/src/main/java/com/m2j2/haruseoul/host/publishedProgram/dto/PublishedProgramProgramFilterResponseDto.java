package com.m2j2.haruseoul.host.publishedProgram.dto;

import com.m2j2.haruseoul.host.program.dto.ProgramFilterListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishedProgramProgramFilterResponseDto {
    private List<ProgramFilterListDto> programFilterListDtos;
}
