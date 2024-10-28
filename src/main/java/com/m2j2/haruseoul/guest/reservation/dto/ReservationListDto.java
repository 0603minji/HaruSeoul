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
    // 문의를 위해서 hostId 를 받아야할듯
    private String statusName;

    private String programTitle;

    private LocalDate date;

    private Integer groupSize;

//    private String src;
}
