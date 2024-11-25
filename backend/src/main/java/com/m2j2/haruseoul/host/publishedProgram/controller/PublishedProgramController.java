package com.m2j2.haruseoul.host.publishedProgram.controller;

import com.m2j2.haruseoul.entity.PublishedProgram;
import com.m2j2.haruseoul.host.program.dto.ProgramFilterListDto;
import com.m2j2.haruseoul.host.publishedProgram.dto.*;
import com.m2j2.haruseoul.host.publishedProgram.service.PublishedProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("host/published-programs")
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

    // distinct pp.program.id from PublishedPrograms pp where pp.program.regMemberId = ?
    @GetMapping("/distinct")
    public ResponseEntity<PublishedProgramProgramFilterResponseDto> getAllDistinct(@RequestParam(required = false) Long mId) {
        return ResponseEntity.ok(service.getProgramFilterList(mId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublishedProgramListDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<PublishedProgramCreatedDto> create(@RequestBody PublishedProgramCreateDto publishedProgramCreateDto) {
        return ResponseEntity.ok(service.create(publishedProgramCreateDto));
    }

    @PutMapping
    public ResponseEntity<PublishedProgramUpdatedDto> update(@RequestBody PublishedProgramUpdateDto publishedProgramUpdateDto) {
        return ResponseEntity.ok(service.update(publishedProgramUpdateDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        boolean isDeleted = service.delete(id);
        if (isDeleted)
            return ResponseEntity.ok("PublishedProgram with id: " + id + " deleted successfully.");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PublishedProgram with id: " + id + " not found.");
    }
}