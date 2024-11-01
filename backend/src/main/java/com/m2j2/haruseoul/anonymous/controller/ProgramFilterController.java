package com.m2j2.haruseoul.anonymous.controller;

import com.m2j2.haruseoul.anonymous.service.DefaultCategoryService;
import com.m2j2.haruseoul.host.program.dto.ProgramTitle;
import com.m2j2.haruseoul.host.program.service.DefaultProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("programFilterController")
@RequestMapping("titles")
public class ProgramFilterController {

    DefaultProgramService service;
    public ProgramFilterController(DefaultProgramService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProgramTitle>> getProgramTitles() {
        List<ProgramTitle> programTitles = service.getProgramTitles();
        return ResponseEntity.ok(programTitles);
    }
}

