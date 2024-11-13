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
public class OnGoingPublishedProgramListDto {
    private List<PublishedProgramListDto> onGoingPrograms;
}
