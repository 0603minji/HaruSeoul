package com.m2j2.haruseoul.guest.reservation.controller;

import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationResponseDto;
import com.m2j2.haruseoul.guest.reservation.service.DefaultReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("guestReservationController")
@RequestMapping("guest/reservations")
public class ReservationController {

    DefaultReservationService reservationService;

    public ReservationController(DefaultReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<ReservationResponseDto> getList(
            @RequestParam(name = "s", required = false)Long sId){
        ReservationResponseDto reservationResponseDto = reservationService.getReservationByStatus(sId);

        return ResponseEntity.ok(reservationResponseDto);
    }
}
