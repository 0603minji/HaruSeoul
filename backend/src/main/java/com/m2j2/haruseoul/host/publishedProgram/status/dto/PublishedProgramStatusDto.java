package com.m2j2.haruseoul.host.publishedProgram.status.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishedProgramStatusDto {
    private Long id;
    private String name;
}
