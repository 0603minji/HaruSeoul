package com.m2j2.haruseoul.anonymous.program.controller;

import com.m2j2.haruseoul.host.publishedProgram.dto.*;
import com.m2j2.haruseoul.host.publishedProgram.service.PublishedProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController("anonymousPublishProgramController")
@RequestMapping("published-programs")
public class PublishedProgramController {

    PublishedProgramService service;

    public PublishedProgramController(PublishedProgramService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PublishedProgramResponseDto> getAll(@RequestParam(name = "mIds", required = false) List<Long> memberIds,
                                                              @RequestParam(name = "d", required = false) List<LocalDate> dates,
                                                              @RequestParam(name = "s", required = false) List<Long> statusIds,
                                                              @RequestParam(name = "pIds", required = false) List<Long> programIds,
                                                              @RequestParam(name = "p", defaultValue = "1") Integer page,
                                                              @RequestParam(name = "pageSize", defaultValue = "42") Integer pageSize,
                                                              @RequestParam(name = "sortBy", defaultValue = "regDate") String sortBy,
                                                              @RequestParam(name = "order", defaultValue = "desc") String order,
                                                              @RequestParam(name = "tab", required = false) String tab,
                                                              @RequestParam(defaultValue = "false") Boolean unpaged) {
        if (unpaged)
            return ResponseEntity.ok(service.getList(memberIds, dates, statusIds, programIds, sortBy, order));
        return ResponseEntity.ok(service.getList(memberIds, dates, statusIds, programIds, page, pageSize, sortBy, order, tab));
    }

}