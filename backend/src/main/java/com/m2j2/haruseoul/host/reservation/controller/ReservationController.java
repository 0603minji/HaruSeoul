package com.m2j2.haruseoul.host.reservation.controller;

import com.m2j2.haruseoul.host.reservation.dto.ReservationListDto;
import com.m2j2.haruseoul.host.reservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            Long rId = reservationService.cancel(reservationId);
            return ResponseEntity.ok("Reservation(id: " + rId + ") cancelled successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 예약이 없을 경우 404 Not Found 반환
        }
    }

    @GetMapping
    public ResponseEntity<List<ReservationListDto>> getList(@RequestParam(name = "ppId") Long publishedProgramId) {
        return ResponseEntity.ok(reservationService.getApplicants(publishedProgramId));
    }
}
