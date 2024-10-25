package com.m2j2.haruseoul.host.publishedProgram.dto;

import com.m2j2.haruseoul.entity.Program;
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
    private Long id;
    private Program program;
    private List<LocalDate> dates;
}
