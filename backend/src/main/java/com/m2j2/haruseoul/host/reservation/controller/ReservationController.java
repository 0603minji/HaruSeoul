package com.m2j2.haruseoul.host.reservation.controller;

import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.host.reservation.dto.*;
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

    @PutMapping("{rid}/cancel")
    public ResponseEntity<String> cancel(@PathVariable(name = "rid") Long reservationId,
                                         @RequestBody ReservationCancelDto dto) {
        try {
            Reservation rv = reservationService.cancel(reservationId, dto);
            return ResponseEntity
                    .ok("Reservation(id: " + rv.getId() + ") cancelled successfully.\ncancelMethod: "+rv.getCancelMethod()+"\ncancelReason: "+rv.getCancelReason());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 예약이 없을 경우 404 Not Found 반환
        }
    }

    @PutMapping("{rid}/reservationStatus")
    public ResponseEntity<String> updateReservationStatus(@PathVariable(name = "rid") Long reservationId,
                                                          @RequestBody ReservationStatusUpdateDto dto) {
        try {
            Reservation rv = reservationService.updateReservationStatus(reservationId, dto);
            return ResponseEntity
                    .ok("Reservation(id: " + rv.getId() + ") ReservationStatus updated successfully.\nReservatoinStauts: "+rv.getReservationStatus());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("consent")
    public ResponseEntity<ReservationConsentUpdatedDto> updateConsent(@RequestBody ReservationConsentUpdateDto dto) {
        return ResponseEntity.ok(reservationService.updateConsent(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReservationListDto>> getList(@RequestParam(name = "ppId") Long publishedProgramId) {
        return ResponseEntity.ok(reservationService.getApplicants(publishedProgramId));
    }
}
