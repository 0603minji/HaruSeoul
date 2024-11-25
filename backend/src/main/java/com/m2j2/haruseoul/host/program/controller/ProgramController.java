package com.m2j2.haruseoul.host.program.controller;

import com.m2j2.haruseoul.anonymous.service.DefaultCategoryService;
import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.host.program.dto.*;
import com.m2j2.haruseoul.host.program.service.DefaultProgramService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController("hostProgramController")
@RequestMapping("host/programs")
public class ProgramController {

    DefaultProgramService service;

    public ProgramController(DefaultProgramService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ProgramResponseDto> getList(
            @RequestParam(name = "mid", required = true) Long mid,
            @RequestParam(name = "c", required = false) List<Long> cIds,
            @RequestParam(name = "pg", required = false) List<Long> pIds,
            @RequestParam(name = "s", required = false) List<String> statuses,
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(name = "cardsPerPage", defaultValue = "6") int cardsPerPage) {

        ProgramResponseDto programResponseDto = service.getList(mid, pIds, cIds, statuses, pageNum, cardsPerPage);

        return ResponseEntity.ok(programResponseDto);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Program> create(
            @RequestPart("programCreateDto") ProgramCreateDto programCreateDto,
            @RequestPart(value = "images",required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(service.create(programCreateDto, images));
    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable(name = "id") Long pId) {
        service.delete(pId);
    }

    @PutMapping("{id}/softDelete")
    public ResponseEntity<Program> softDelete(@PathVariable(name = "id") Long pId) {
        return ResponseEntity.ok(service.softDelete(pId));
    }

    @PutMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Program> update(@RequestPart("programUpdateDto") ProgramUpdateDto programUpdateDto,
                                          @RequestPart(value = "images",required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(service.update(programUpdateDto, images));
    }


    @GetMapping("{id}")
    public ResponseEntity<ProgramEditDto> getProgram(@PathVariable(name = "id") Long pId) {
        return ResponseEntity.ok(service.getProgram(pId));
    }


    @GetMapping("user/{id}")
    public ResponseEntity<List<ProgramFilterListDto>> getList(@PathVariable(name = "id") Long hostId,
                                                              @RequestParam(required = false) List<Long> pIds,
                                                              @RequestParam(name = "s", required = false) List<String> statuses) {
        return ResponseEntity.ok(service.getList(hostId, pIds, statuses));
    }

    @PutMapping("{id}/statusCheck")
    public ResponseEntity<Program> statusCheck(@PathVariable(name = "id") Long pId) {
        return ResponseEntity.ok(service.statusCheck(pId));
    }
}
