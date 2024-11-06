package com.m2j2.haruseoul.anonymous.program.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramDetailResponseDto {


    // ====Route 테이블====
    private LocalTime startTime;
    private String routeTitle;
    private Integer routeHour;
    private Integer routeMinute;
    private String transportation;
    private LocalTime transportTime;
    private String description;
    private String address;



    //=====Review 테이블========
    private Float reviewRating;
    private String reviewRegMemberProfileImg;
    private String reviewRegMemberName;
    private LocalDate reviewRegDate;
    private String reviewContent;

    private Integer ratingCount;

}
