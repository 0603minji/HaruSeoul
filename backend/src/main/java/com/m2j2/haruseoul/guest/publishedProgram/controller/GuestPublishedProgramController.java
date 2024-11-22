package com.m2j2.haruseoul.guest.publishedProgram.controller;

import com.m2j2.haruseoul.guest.publishedProgram.dto.GuestPublishedProgramUpdateDto;
import com.m2j2.haruseoul.guest.publishedProgram.dto.GuestPublishedProgramUpdatedDto;
import com.m2j2.haruseoul.guest.publishedProgram.service.GuestPublishedProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("guest/published-programs")
public class GuestPublishedProgramController {

    private GuestPublishedProgramService guestPublishedProgramService;

    public GuestPublishedProgramController(GuestPublishedProgramService guestPublishedProgramService) {
        this.guestPublishedProgramService = guestPublishedProgramService;
    }

    @PutMapping
    public ResponseEntity<GuestPublishedProgramUpdatedDto> update(
            @RequestBody GuestPublishedProgramUpdateDto guestPublishedProgramUpdateDto) {
        return ResponseEntity.ok(guestPublishedProgramService.update(guestPublishedProgramUpdateDto));
    }
}