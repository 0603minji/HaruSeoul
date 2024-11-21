package com.m2j2.haruseoul.guest.reservation.controller;

import com.m2j2.haruseoul.guest.reservation.dto.ReservationCreateDto;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationCreatedDto;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationDetailResponseDto;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationResponseDto;
import com.m2j2.haruseoul.guest.reservation.service.DefaultReservationService;
import org.springframework.http.HttpStatus;
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
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum){
        ReservationResponseDto reservationResponseDto = reservationService.getList(statusIds, memberIds, pageNum);

        return ResponseEntity.ok(reservationResponseDto);
    }

    @GetMapping("/{rid}")
    public ResponseEntity<ReservationDetailResponseDto> getDetail(
            @PathVariable("rid") Long rId){
        System.out.println("Received rId: " + rId);
        ReservationDetailResponseDto reservationDetailResponseDto = reservationService.getDetail(rId);

        return ResponseEntity.ok(reservationDetailResponseDto);
    }

    @PostMapping
    public ResponseEntity<ReservationCreatedDto> create(@RequestBody ReservationCreateDto reservationCreateDto) {
        return ResponseEntity.ok(reservationService.create(reservationCreateDto));
    }

    @PutMapping("{rid}/cancel")  // 예약 취소를 위한 PUT 요청
    public ResponseEntity<Void> cancelReservation(@PathVariable(name = "rid") Long reservationId) {
        try {
            // delete 메서드를 호출하여 예약 취소 처리 (소프트 삭제)
            reservationService.delete(reservationId);
            return ResponseEntity.noContent().build();  // 204 No Content 응답 반환 (취소 성공)
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 예약이 없을 경우 404 Not Found 반환
        }
    }
}
