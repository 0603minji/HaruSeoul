package com.m2j2.haruseoul.guest.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCreatedDto {

    private Long programId;

    private Long regMemberId;

    private LocalDate reservationDate;

    private Integer reservationGroupSize;

    private String reservationRequirement;
}
