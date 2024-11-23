package com.m2j2.haruseoul.guest.reservation.dto;

import com.m2j2.haruseoul.entity.PublishedProgram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {
    private Long totalRowCount;
    private Long totalPageCount;
    private List<ReservationListDto> reservations;
}