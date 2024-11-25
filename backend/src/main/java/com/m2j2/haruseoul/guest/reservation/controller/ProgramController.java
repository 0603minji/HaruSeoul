package com.m2j2.haruseoul.guest.reservation.controller;

import com.m2j2.haruseoul.host.publishedProgram.dto.OnGoingPublishedProgramListDto;
import com.m2j2.haruseoul.host.publishedProgram.service.DefaultPublishedProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("guestProgramController")
@RequestMapping("programs")
public class ProgramController {

    DefaultPublishedProgramService defaultPublishedProgramService;

    public ProgramController(DefaultPublishedProgramService defaultPublishedProgramService) {
        this.defaultPublishedProgramService = defaultPublishedProgramService;
    }

    @GetMapping("{pId}/reservation")
    public ResponseEntity<OnGoingPublishedProgramListDto> findByProgramId(@PathVariable Long pId) {
        return ResponseEntity.ok(defaultPublishedProgramService.findByProgramId(pId));
    }
}
