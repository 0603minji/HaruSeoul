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

    private Long programId;
    
    private String statusName;

    private String title;

    private LocalDate date;

    private Integer groupSizeCurrent;

//    private String src;
}
