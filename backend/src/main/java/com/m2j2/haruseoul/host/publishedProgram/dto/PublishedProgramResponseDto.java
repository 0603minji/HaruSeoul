package com.m2j2.haruseoul.host.publishedProgram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishedProgramResponseDto {
    private List<Long> pages;
    private Integer startNum;
    private long totalCount;
    private long totalPages;
    private int currentPageRowCount;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private List<PublishedProgramListDto> publishedPrograms;
}
