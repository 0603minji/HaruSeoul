package com.m2j2.haruseoul.anonymous.program.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramDetailRouteDto {
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    private String title;
    private Integer durationHour;
    private Integer durationMinute;
    private String transportation;
    private LocalTime transportTime;
    private String description;
    private String address;
    private Integer order;
}
