package com.m2j2.haruseoul.host.program.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RouteCreateDto {
    private Long transportationId;
    private Integer order;
    private String title;
    private String address;
    private String description;
    private LocalTime duration;
    private LocalTime transportationDuration;
}
