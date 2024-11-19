package com.m2j2.haruseoul.host.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationListDto {
    private Long reservationId;
    private Long memberId;
    private String memberNickname;
    private String applicantName;
    private String email;
    private String phone;
    private String requirement;
}
