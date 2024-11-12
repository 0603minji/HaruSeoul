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
public class ReservationCreateDto {

    private Long publishedProgramId;

    private Long regMemberId;

    private LocalDate pickedDate;

    private Integer reservationGroupSize;

    private String reservationRequirement;

}
