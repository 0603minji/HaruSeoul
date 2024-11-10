package com.m2j2.haruseoul.host.program.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RouteCreateDto {
    private Long id;
    private Long transportationId;
    private Integer order;
    private String title;
    private String address;
    private String description;
    private Integer duration;
    private String transportationDuration;
    private String startTimeHour;
    private String startTimeMinute;
}
