package com.m2j2.haruseoul.guest.reservation.service;

import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationCreateDto;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationDetailDto;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {
    ReservationResponseDto getList(List<Long> sIds, List<Long> mIds);

    // Detail 에 대한 페이지는 페이징할 필요없이 1개만 담는 것이므로 Response 로 따로 담지 않는다.
    ReservationDetailDto getDetail(Long rid);

    Reservation create(ReservationCreateDto reservationCreateDto);

    void delete(Long reservationId);
}
