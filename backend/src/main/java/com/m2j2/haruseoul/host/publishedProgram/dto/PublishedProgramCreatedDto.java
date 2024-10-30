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
public class PublishedProgramCreatedDto {
    private Long programId;
    private String programTitle;
    private Long regMemberId;
    private List<LocalDate> dates;
}
