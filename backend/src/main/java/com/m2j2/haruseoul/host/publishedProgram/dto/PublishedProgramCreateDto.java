package com.m2j2.haruseoul.host.publishedProgram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishedProgramCreateDto {
    private Long regMemberId;
    private Long programId;
    private List<LocalDate> dates;
}
