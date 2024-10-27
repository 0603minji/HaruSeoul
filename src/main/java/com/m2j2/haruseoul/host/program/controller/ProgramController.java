package com.m2j2.haruseoul.host.program.controller;

import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.host.program.dto.ProgramCreateDto;
import com.m2j2.haruseoul.host.program.dto.ProgramResponseDto;

import com.m2j2.haruseoul.anonymous.service.DefaultCategoryService;
import com.m2j2.haruseoul.host.program.dto.ProgramTitle;
import com.m2j2.haruseoul.host.program.dto.ProgramUpdateDto;
import com.m2j2.haruseoul.host.program.service.DefaultProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("hostProgramController")
@RequestMapping("host/programs")
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
            @RequestParam(name = "s", required = false) List<String> statuses) {
        ProgramResponseDto programResponseDto = service.getList(pIds, cIds, statuses);

        return ResponseEntity.ok(programResponseDto);
    }

    @GetMapping("/titles")
    public ResponseEntity<List<ProgramTitle>> getProgramTitles() {
        List<ProgramTitle> programTitles = service.getProgramTitles();
        return ResponseEntity.ok(programTitles);
    }

    @PostMapping
    public ResponseEntity<Program> create(
            @RequestBody ProgramCreateDto programCreateDto
    ) {
        return ResponseEntity.ok(service.create(programCreateDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Program> update(
            @RequestBody ProgramUpdateDto programUpdateDto
    ) {
        return ResponseEntity.ok(service.update(programUpdateDto));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        service.delete(id);
    }

}
