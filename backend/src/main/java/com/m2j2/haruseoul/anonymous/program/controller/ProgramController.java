package com.m2j2.haruseoul.anonymous.program.controller;

import com.m2j2.haruseoul.anonymous.program.dto.ProgramFilterDto;
import com.m2j2.haruseoul.anonymous.program.dto.ProgramResponseDto;
import com.m2j2.haruseoul.anonymous.program.service.ProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("anonymousProgramController")
@RequestMapping("programs")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }


    @GetMapping
    public ResponseEntity<ProgramResponseDto> getList(@ModelAttribute ProgramFilterDto programFilterDto,
                                                      @RequestParam(defaultValue = "1") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(programService.getList(programFilterDto.getProgramIds(),
                programFilterDto.getCategoryIds(),
                programFilterDto.getStartDate(),
                programFilterDto.getEndDate(),
                programFilterDto.getMinPrice(),
                programFilterDto.getMaxPrice(),
                programFilterDto.getGroupSizeMax(),
                programFilterDto.getGroupSizeMin(),
                programFilterDto.getDuration(),
                programFilterDto.getStartTime(),
                programFilterDto.getLanguage(),
                page,pageSize));
    }

}
