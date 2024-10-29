package com.m2j2.haruseoul.guest.reservation.dto;

import com.m2j2.haruseoul.entity.PublishedProgram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCreateDto {

    private Long publishedProgramId;

    private Long regMemberId;

    private Instant programDate;

    private Integer reservationGroupSize;

}
