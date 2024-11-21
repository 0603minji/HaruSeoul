package com.m2j2.haruseoul.anonymous.reservation.service;

import com.m2j2.haruseoul.anonymous.reservation.dto.ShareReservationDetailResponseDto;

public interface ShareReservationService {
    ShareReservationDetailResponseDto getDetail(Long rid);
}
