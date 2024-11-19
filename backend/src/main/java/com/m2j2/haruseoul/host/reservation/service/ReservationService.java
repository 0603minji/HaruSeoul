package com.m2j2.haruseoul.host.reservation.service;

import com.m2j2.haruseoul.host.reservation.dto.ReservationListDto;

import java.util.List;

public interface ReservationService {
    Long cancel(Long rid);
    List<ReservationListDto> getApplicants(Long ppId);
}