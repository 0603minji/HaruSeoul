package com.m2j2.haruseoul.guest.reservation.controller;

import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.guest.reservation.dto.*;
import com.m2j2.haruseoul.guest.reservation.service.DefaultReservationService;
import com.m2j2.haruseoul.host.program.dto.ProgramCreateDto;
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
            @RequestParam(name = "s", required = false)List<Long> statusIds,
            @RequestParam(name = "m", required = false)List<Long> memberIds,
            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(name = "cardsPerPage", defaultValue = "6") int cardsPerPage){
        ReservationResponseDto reservationResponseDto = reservationService.getList(statusIds, memberIds, pageNumber, cardsPerPage);

        return ResponseEntity.ok(reservationResponseDto);
    }

    @GetMapping("{rid}")
    public ResponseEntity<ReservationDetailResponseDto> getDetail(
            @PathVariable("rid") Long rId){
        ReservationDetailResponseDto reservationDetailResponseDto = reservationService.getDetail(rId);

        return ResponseEntity.ok(reservationDetailResponseDto);
    }

    @PostMapping
    @RequestMapping("/new")
    public void create(
            @RequestBody ReservationCreateDto reservationCreateDto){
        reservationService.create(reservationCreateDto);
    }
}
