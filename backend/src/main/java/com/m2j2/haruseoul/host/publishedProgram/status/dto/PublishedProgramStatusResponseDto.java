package com.m2j2.haruseoul.host.publishedProgram.status.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishedProgramStatusResponseDto {
    private List<PublishedProgramStatusDto> statusDtos;
}
