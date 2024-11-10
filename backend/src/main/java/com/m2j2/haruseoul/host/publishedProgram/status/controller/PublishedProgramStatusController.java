package com.m2j2.haruseoul.host.publishedProgram.status.controller;

import com.m2j2.haruseoul.host.publishedProgram.status.dto.PublishedProgramStatusResponseDto;
import com.m2j2.haruseoul.host.publishedProgram.status.service.DefaultPublishedProgramStatusService;
import com.m2j2.haruseoul.host.publishedProgram.status.service.PublishedProgramStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("host/published-programs/status")
public class PublishedProgramStatusController {

    PublishedProgramStatusService service;

    public PublishedProgramStatusController(PublishedProgramStatusService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PublishedProgramStatusResponseDto> getAll() {
        return ResponseEntity.ok(service.getList());
    }
}
