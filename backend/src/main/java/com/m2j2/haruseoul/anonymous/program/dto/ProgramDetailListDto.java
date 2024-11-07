package com.m2j2.haruseoul.anonymous.program.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramDetailListDto {

    private List<String> img;
    private List<String> categoryNames;

    private String title;
    private Float rating;

    private Integer price;
    private String language;
    private Integer groupSizeMin;
    private Integer groupSizeMax;
    private String meetingPoint;
    private String hostName;
    private String hostProfileImg;
    private Float hostRating;
    private Integer hostRatingCount;
    private String detail;
    private String inclusion;
    private String exclusion;
    private String packingList;
    private String caution;

}
