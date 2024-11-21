package com.m2j2.haruseoul.guest.reservation.service;

import com.m2j2.haruseoul.guest.reservation.dto.*;

import java.util.List;

public interface ReservationService {
    ReservationResponseDto getList(List<Long> sIds, List<Long> mIds, int pageNum);

    ReservationDetailResponseDto getDetail(Long rid);

    ReservationCreatedDto create(ReservationCreateDto reservationCreateDto);

    void delete(Long reservationId);
}
