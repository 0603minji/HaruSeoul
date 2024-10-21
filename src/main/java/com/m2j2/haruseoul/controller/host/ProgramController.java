package com.m2j2.haruseoul.controller.host;

import com.m2j2.haruseoul.dto.ProgramResponseDto;
import com.m2j2.haruseoul.service.DefaultProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("hostProgramController")
@RequestMapping("host/programs")
@CrossOrigin
public class ProgramController {
    DefaultProgramService service;

    public ProgramController(DefaultProgramService defaultProgramService) {
        this.service = defaultProgramService;
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
