package com.m2j2.haruseoul.host.publishedProgram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishedProgramCreatedDto {
    private Long regMemberId;
    private Long programId;
}
