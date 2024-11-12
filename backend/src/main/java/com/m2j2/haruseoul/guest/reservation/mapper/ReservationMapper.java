package com.m2j2.haruseoul.guest.reservation.mapper;

import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationListDto;

import java.util.Date;

public class ReservationMapper {

    public static ReservationListDto mapToDto(Reservation reservation) {

        return ReservationListDto.builder()
                .id(reservation.getId())
                .hostId(reservation.getPublishedProgram().getProgram().getMember().getId())
                .statusName(reservation.getPublishedProgram().getStatus().getName())
                .programTitle(reservation.getPublishedProgram().getProgram().getTitle())
                .date(reservation.getPublishedProgram().getDate())
                .groupSize(reservation.getPublishedProgram().getGroupSizeCurrent())
                .build();
    }
}
