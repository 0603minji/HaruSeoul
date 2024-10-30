package com.m2j2.haruseoul.guest.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetailDto {

    private Long reservationId;

    //memberName, memberEmail
    private String memberName;

    private String memberEmail;

      // member 테이블에 없는데 만들어야 할듯
//    private String guestPhoneNumber; // +82- 이런식이라 String 으로 담아야 하나?

//    private Date PaymentDate; // 결제일

//    private String PaymentMethod; // 결제수단 (Credit card 와 카드번호 같이 담아야하는데 List?)

    private String hostName;

    private String programRating; // 프로그램 평점 합계

    private Long reviewsCount; // 평점 개수

    private String programStartTime;

    private String routeTitle; // 만나는 장소 요약 제목

    private String routeAddress;

    private String programInclusion;

    private String programExclusion;

    private String programPackingList;

    private String programCaution;

    private String reservationRequirement;
}
