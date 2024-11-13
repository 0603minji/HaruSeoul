package com.m2j2.haruseoul.host.program.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteCreateDto {

    private Long transportationId;
    private Integer order;
    private String title;
    private String address;
    private String description;
    private Integer duration;
    private String transportationDuration;
    private String transportationName;
    private String startTimeHour;
    private String startTimeMinute;
}
