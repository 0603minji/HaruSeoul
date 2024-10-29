package com.m2j2.haruseoul.guest.reservation.controller;

import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationDetailDto;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationDetailResponseDto;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationResponseDto;
import com.m2j2.haruseoul.guest.reservation.service.DefaultReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(name = "s", required = false)List<Long> sIds,
            @RequestParam(name = "m", required = false)List<Long> mIds){
        ReservationResponseDto reservationResponseDto = reservationService.getList(sIds, mIds);

        return ResponseEntity.ok(reservationResponseDto);
    }

    @GetMapping("{rid}")
    public ResponseEntity<ReservationDetailResponseDto> getDetail(
            @PathVariable("rid") Long rId){
        ReservationDetailResponseDto reservationDetailResponseDto = reservationService.getDetail(rId);

        return ResponseEntity.ok(reservationDetailResponseDto);
    }
}
