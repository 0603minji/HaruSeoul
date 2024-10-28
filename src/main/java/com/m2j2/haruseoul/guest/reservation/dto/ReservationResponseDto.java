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
    private Long totalRowCount;
    private Long totalPageCount;
    private List<ReservationListDto> reservations;
    // guest 가 예약하기 누를때 program.requirement 가 팝업으로 뜨면서 주의사항을 알려준다고 했는데, 여기에 담아줘야 하는가
}