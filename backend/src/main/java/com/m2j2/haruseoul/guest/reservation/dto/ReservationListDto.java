package com.m2j2.haruseoul.guest.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationListDto {

    private Long id;
    
    private Long hostId; // host 문의

    private String statusName;

    private String programTitle;

    private LocalDate date;

    private Integer groupSize;

//    private String src;
}
