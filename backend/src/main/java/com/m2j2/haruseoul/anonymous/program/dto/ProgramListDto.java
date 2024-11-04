package com.m2j2.haruseoul.anonymous.program.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramListDto {
    private Long id;
    private String title;
    private List<String> categoryNames;
    private Integer price;
    private Float rating;
    private Integer totalRatingCount;
    private Float duration;
    private String img;
}
