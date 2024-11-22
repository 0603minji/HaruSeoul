package com.m2j2.haruseoul.guest.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetailProgramDto {

    private Long programId;

    private String programStartTime;

    private String meetingSpotTitle; // 만나는 장소 요약 제목

    private String meetingSpotAddress;

    private String programInclusion;

    private String programExclusion;

    private String programPackingList;

    private String programCaution;

    private String reservationRequirement;

    private Long publishedProgramId;

    private Long publishedProgramStatus;

}
