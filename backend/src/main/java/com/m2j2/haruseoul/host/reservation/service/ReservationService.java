package com.m2j2.haruseoul.host.reservation.service;

import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.host.reservation.dto.*;

import java.util.List;

public interface ReservationService {
    Reservation cancel(Long rid, ReservationCancelDto dto);
    List<ReservationListDto> getApplicants(Long ppId);
    ReservationConsentUpdatedDto updateConsent(ReservationConsentUpdateDto dto);
    Reservation updateReservationStatus(Long rId, ReservationStatusUpdateDto dto);
}