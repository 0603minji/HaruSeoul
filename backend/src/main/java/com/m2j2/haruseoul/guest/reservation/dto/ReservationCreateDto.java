package com.m2j2.haruseoul.guest.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCreateDto {

    private Long publishedProgramId;

    private Long guestId;

    private Integer numberOfGuest;

    private String reservationRequirement;
}
