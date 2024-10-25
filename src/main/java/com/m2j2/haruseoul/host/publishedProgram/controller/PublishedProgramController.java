package com.m2j2.haruseoul.host.publishedProgram.controller;

import com.m2j2.haruseoul.host.publishedProgram.dto.PublishedProgramCreateDto;
import com.m2j2.haruseoul.host.publishedProgram.dto.PublishedProgramCreatedDto;
import com.m2j2.haruseoul.host.publishedProgram.service.PublishedProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("host/published-programs")
public class PublishedProgramController {

    PublishedProgramService service;

    public PublishedProgramController(PublishedProgramService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PublishedProgramCreatedDto> create(@RequestBody PublishedProgramCreateDto publishedProgramCreateDto) {
        return ResponseEntity.ok(service.create(publishedProgramCreateDto));
    }
}
