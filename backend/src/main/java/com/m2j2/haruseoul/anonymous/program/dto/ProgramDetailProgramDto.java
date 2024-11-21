package com.m2j2.haruseoul.anonymous.program.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramDetailProgramDto {
    private Long id;
    private String title;
    private Float rating;
    private Integer price;
    private String language;
    private Integer groupSizeMin;
    private Integer groupSizeMax;
    private String detail;
    private String inclusion;
    private String exclusion;
    private String packingList;
    private String caution;
    private Float duration;
    private LocalTime startTime;
    private String status;

}
