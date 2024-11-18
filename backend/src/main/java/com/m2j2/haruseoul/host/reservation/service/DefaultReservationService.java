package com.m2j2.haruseoul.host.reservation.service;

import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DefaultReservationService implements ReservationService {

    ReservationRepository reservationRepository;

    public DefaultReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void cancel(Long id) {
        // 예약 ID로 해당 예약을 찾아옵니다.
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        // 삭제 날짜를 현재 시간으로 설정합니다.
        reservation.setDeleteDate(Instant.now());  // 삭제 일자를 현재 시간으로 설정 (LocalDateTime으로 변경 가능)

        // 예약을 업데이트합니다.
        reservationRepository.save(reservation);

        // 로그를 남겨서 예약 삭제에 대한 기록을 남길 수 있습니다.
        // System.out.println("Reservation with ID " + reservationId + " has been soft deleted at " + Instant.now());
    }
}
