package com.m2j2.haruseoul.guest.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {
    private String sort;
    private Long totalRowCount;
    private Long totalPageCount;
    private List<ReservationListDto> reservations;
}
