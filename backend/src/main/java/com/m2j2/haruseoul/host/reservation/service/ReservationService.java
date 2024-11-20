package com.m2j2.haruseoul.host.reservation.service;

import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.host.reservation.dto.ReservationListDto;
import com.m2j2.haruseoul.host.reservation.dto.ReservationCancelDto;

import java.util.List;

public interface ReservationService {
    Reservation cancel(Long rid, ReservationCancelDto dto);
    List<ReservationListDto> getApplicants(Long ppId);
}