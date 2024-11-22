package com.m2j2.haruseoul.host.program.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDetailDto {

    private Long id;
    private String title;
    private String detail;
    private Instant regDate;
    private Instant endTime;
    private String status;
    private Integer price;
    private Integer groupSizeMax;
    private Integer groupSizeMin;
    private Float rating;
    private Instant updateDate;
    private String language;
    private Instant startTime;
    private List<String> categoryNames;
    private Long memberId;

//    private List<Image> images;
//
//    private List<Review> reviews;

}
