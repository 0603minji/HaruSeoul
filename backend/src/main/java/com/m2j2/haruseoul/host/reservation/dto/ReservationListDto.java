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
    private Integer numberOfGuest;
    private String guestProfileImgSrc;
    private String email;
    private String phone;
    private String hostRequirement; // host가 프로그램 등록 시 게스트에게 요청한 질문
    private String requirement; // guest가 결제할 때 작성한 답
    private Integer guestConsent;
}
