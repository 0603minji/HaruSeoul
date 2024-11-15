package com.m2j2.haruseoul.guest.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationListDto {

    private Long id;
    
    private Long hostId; // host 문의

    // publishedProgram 의 status name
    private String statusName;

    private String programTitle;

    private LocalDate date;

    private Integer numberOfGuest;

    private String src;
}
