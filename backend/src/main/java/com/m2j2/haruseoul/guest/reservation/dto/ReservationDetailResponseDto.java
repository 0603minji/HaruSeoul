package com.m2j2.haruseoul.guest.reservation.dto;

import com.m2j2.haruseoul.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetailResponseDto {

    private Long reservationId;

    private ReservationListDto reservationCard;

    private ReservationDetailProgramDto program;

    private ReservationDetailGuestDto guest;

    private ReservationDetailHostDto host;

//    private ReservationDetailPaymentDto payment;

}
