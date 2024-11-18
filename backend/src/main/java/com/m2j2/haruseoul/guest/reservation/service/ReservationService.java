package com.m2j2.haruseoul.guest.reservation.service;

import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.guest.reservation.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {
    ReservationResponseDto getList(List<Long> sIds, List<Long> mIds, Boolean isDeleted, int pageNum);

    ReservationDetailResponseDto getDetail(Long rid);

    ReservationCreatedDto create(ReservationCreateDto reservationCreateDto);

    void delete(Long reservationId);
}
