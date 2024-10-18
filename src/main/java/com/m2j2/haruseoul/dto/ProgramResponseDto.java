package com.m2j2.haruseoul.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramResponseDto {

    private List<ProgramDto> programs;

    private String sort;

    private Long totalRowCount;

    private Long totalPageCount;
}
