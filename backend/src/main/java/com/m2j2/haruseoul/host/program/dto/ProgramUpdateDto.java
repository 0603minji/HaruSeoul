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
public class ProgramUpdateDto {

    private Long programId;

    private String title;

    private String detail;

    private List<Long> categoryIds;

    private String language;

    private String startTime;

    private String endTime;

    private Integer groupSizeMax;

    private Integer groupSizeMin;

    private Integer price;

    private List<RouteCreateDto> routes;

    private String inclusion;

    private String exclusion;

    private String packingList; // 준비물

    private String caution; // 주의사항

    private String requirement; // 요청사항

    //private List<Image> images;

}