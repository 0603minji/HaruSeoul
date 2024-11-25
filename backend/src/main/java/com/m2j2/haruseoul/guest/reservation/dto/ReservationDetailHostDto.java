package com.m2j2.haruseoul.guest.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetailHostDto {

    private String memberImg; // 프사

    private String memberName;

    private String programRating; // 프로그램 평점 합계

    private Long ratingCount; // 평점 개수

}
