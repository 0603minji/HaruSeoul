package com.m2j2.haruseoul.guest.publishedProgram.controller;

import com.m2j2.haruseoul.guest.publishedProgram.dto.PublishedProgramDto;
import com.m2j2.haruseoul.guest.publishedProgram.service.PublishedProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("guest/published-programs")
public class PublishedProgramController {

    PublishedProgramService service;

    public PublishedProgramController(PublishedProgramService service) {
        this.service = service;
    }

    @PutMapping
    public ResponseEntity<PublishedProgramDto> update(@RequestBody PublishedProgramDto publishedProgramDto) {
        return ResponseEntity.ok(service.update(publishedProgramDto));
    }
}