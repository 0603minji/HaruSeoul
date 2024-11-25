package com.m2j2.haruseoul.host.program.dto;

import com.m2j2.haruseoul.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramListDto {

    private Long id;
    private String title;
    private String detail;
    private Instant regDate;
    private LocalTime endTime;
    private String endTimeHour;
    private String endTimeMinute;
    private LocalTime startTime;
    private String startTimeHour;
    private String startTimeMinute;
    private String status;
    private Integer price;
    private Integer groupSizeMax;
    private Integer groupSizeMin;
    private Float rating;
    private Instant updateDate;
    private String language;
    private List<String> categoryNames;
    private Long memberId;
    private List<RouteCreateDto> route;
    private String inclusion;
    private String exclusion;
    private String packingList;
    private String caution;
    private String requirement;
    private List<Image> images;

}
