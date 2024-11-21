package com.m2j2.haruseoul.anonymous.reservation.controller;

import com.m2j2.haruseoul.anonymous.reservation.dto.ShareReservationDetailResponseDto;
import com.m2j2.haruseoul.anonymous.reservation.service.DefaultShareReservationService;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationDetailResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("anonymousShareReservationController")
@RequestMapping("share")
public class ShareReservationController {

    DefaultShareReservationService shareReservationService;

    public ShareReservationController(DefaultShareReservationService shareReservationService) {
        this.shareReservationService = shareReservationService;
    }

    @GetMapping("/{rid}")
    public ResponseEntity<ShareReservationDetailResponseDto> getDetail(
            @PathVariable("rid") Long rId) {
        System.out.println("Received rId: " + rId);
        ShareReservationDetailResponseDto shareReservationDetailResponseDto = shareReservationService.getDetail(rId);

        return ResponseEntity.ok(shareReservationDetailResponseDto);
    }
}
