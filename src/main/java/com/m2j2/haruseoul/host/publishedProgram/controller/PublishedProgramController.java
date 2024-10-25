package com.m2j2.haruseoul.host.publishedProgram.controller;

import com.m2j2.haruseoul.entity.PublishedProgram;
import com.m2j2.haruseoul.host.program.service.ProgramService;
import com.m2j2.haruseoul.host.publishedProgram.dto.PublishedProgramCreateDto;
import com.m2j2.haruseoul.host.publishedProgram.service.PublishedProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("host/publishedPrograms")
public class PublishedProgramController {

    PublishedProgramService service;

    @PostMapping
    public ResponseEntity<PublishedProgramCreateDto> create(@RequestBody PublishedProgramCreateDto publishedProgramCreateDto) {


        return ResponseEntity.ok(service.create(publishedProgramCreateDto));
    }
}
