package com.m2j2.haruseoul.host.reservation.controller;

import com.m2j2.haruseoul.host.reservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("HostReservationController")
@RequestMapping("host/reservations")
public class ReservationController {

    ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PutMapping("{rid}")
    public ResponseEntity<String> cancel(@PathVariable(name = "rid") Long reservationId) {
        try {
            reservationService.cancel(reservationId);
            return ResponseEntity.ok("Reservation cancelled successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 예약이 없을 경우 404 Not Found 반환
        }
    }
}
