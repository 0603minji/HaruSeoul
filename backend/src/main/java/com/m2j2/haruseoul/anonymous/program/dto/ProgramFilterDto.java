package com.m2j2.haruseoul.anonymous.program.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramFilterDto {

    private List<Long> programIds;
    private List<Long> categoryIds;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer groupSizeMin;
    private Integer groupSizeMax;
    private Integer duration;
    private Integer startTime;
    private String language;
}
