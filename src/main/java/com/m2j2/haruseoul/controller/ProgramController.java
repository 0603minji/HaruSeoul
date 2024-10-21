package com.m2j2.haruseoul.controller;

import com.m2j2.haruseoul.dto.ProgramResponseDto;
import com.m2j2.haruseoul.service.DefaultProgramService;
import com.m2j2.haruseoul.service.ProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("programController")
@RequestMapping("program")
public class ProgramController {

    DefaultProgramService service;

    public ProgramController(DefaultProgramService defaultProgramService) {
        this.service = defaultProgramService;
    }

    @GetMapping("detail")
    public String detail(){
        return "program/detail";
    }

    @GetMapping("list")
    public String list(){
        return "program/list";
    }

    @GetMapping("pay")
    public String pay(){
        return "program/pay";
    }

    @GetMapping("api/v1/programs")
    @ResponseBody
    public ResponseEntity<ProgramResponseDto> getList(
            @RequestParam(name = "c", required = false) List<Long> cIds,
            @RequestParam(name = "pg", required = false) List<Long> pIds,
            @RequestParam(name = "s", required = false) List<String> statuses){
        ProgramResponseDto programResponseDto = service.getList(pIds, cIds, statuses);
        return ResponseEntity.ok(programResponseDto);
    }
}
