package com.m2j2.haruseoul.anonymous.reservation.dto;

import com.m2j2.haruseoul.guest.reservation.dto.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareReservationDetailResponseDto {

    private Long reservationId;

    private ReservationListDto reservationCard;

    private ReservationDetailRequirementDto requirement;

    private ReservationDetailProgramDto program;

    private ReservationDetailGuestDto guest;

    private ReservationDetailHostDto host;
}
