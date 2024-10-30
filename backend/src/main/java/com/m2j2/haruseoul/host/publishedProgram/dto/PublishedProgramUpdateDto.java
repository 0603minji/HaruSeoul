package com.m2j2.haruseoul.host.publishedProgram.dto;

import com.m2j2.haruseoul.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishedProgramUpdateDto {
    private Long id;
    private Integer groupSizeCurrent;
    private Long statusId;
}