package com.m2j2.haruseoul.host.program.controller;

import com.m2j2.haruseoul.host.program.dto.ProgramResponseDto;

import com.m2j2.haruseoul.anonymous.service.DefaultCategoryService;
import com.m2j2.haruseoul.host.program.service.DefaultProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("hostProgramController")
@RequestMapping("host/programs")
@CrossOrigin
public class ProgramController {

    DefaultProgramService service;
    DefaultCategoryService categoryService;

    public ProgramController(DefaultProgramService service, DefaultCategoryService categoryService) {
        this.service = service;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ProgramResponseDto> getList(
            @RequestParam(name = "c", required = false) List<Long> cIds,
            @RequestParam(name = "pg", required = false) List<Long> pIds,
            @RequestParam(name = "s", required = false) List<String> statuses){
        ProgramResponseDto programResponseDto = service.getList(pIds, cIds, statuses);

        return ResponseEntity.ok(programResponseDto);
    }

}
