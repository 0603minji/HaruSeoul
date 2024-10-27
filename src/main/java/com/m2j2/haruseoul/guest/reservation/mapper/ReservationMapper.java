package com.m2j2.haruseoul.guest.reservation.mapper;

import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationListDto;

import java.util.Date;

public class ReservationMapper {

    public static ReservationListDto mapToDto(Reservation reservation) {
        return ReservationListDto.builder()
                .programId(reservation.getPublishedProgram().getProgram().getId())
                .statusName(reservation.getPublishedProgram().getStatus().getName())
                .title(reservation.getPublishedProgram().getProgram().getTitle())
                .date(reservation.getPublishedProgram().getDate())
                .groupSizeCurrent(reservation.getPublishedProgram().getGroupSizeCurrent())
                .build();
    }
}
