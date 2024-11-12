package com.m2j2.haruseoul.host.program.controller;

import com.m2j2.haruseoul.anonymous.service.DefaultCategoryService;
import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.host.program.dto.*;
import com.m2j2.haruseoul.host.program.service.DefaultProgramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController("hostProgramController")
@RequestMapping("host/programs")
public class ProgramController {

    DefaultProgramService service;

    public ProgramController(DefaultProgramService service, DefaultCategoryService categoryService) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ProgramResponseDto> getList(
            @RequestParam(name = "mid" ,required = true) Long mid,
            @RequestParam(name = "c", required = false) List<Long> cIds,
            @RequestParam(name = "pg", required = false) List<Long> pIds,
            @RequestParam(name = "s", required = false) List<String> statuses,
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(name = "cardsPerPage", defaultValue = "6") int cardsPerPage) {

        ProgramResponseDto programResponseDto = service.getList(mid,pIds, cIds, statuses, pageNum, cardsPerPage);

        return ResponseEntity.ok(programResponseDto);
    }

    @PostMapping
    public ResponseEntity<Program> create(@RequestBody ProgramCreateDto programCreateDto) {
        return ResponseEntity.ok(service.create(programCreateDto));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        service.delete(id);
    }

    //  ====== 호스트 프로그램 수정 컨트롤러 =======================
    @PutMapping
    public ResponseEntity<Program> update(
            @RequestBody ProgramUpdateDto programUpdateDto
    ) {
        return ResponseEntity.ok(service.update(programUpdateDto));
    }

    //  ====== 호스트 프로그램 1건 조회 컨트롤러 =======================
    @GetMapping("{id}")
    public ResponseEntity<ProgramListDto> getOneProgram(@PathVariable(name = "id") Long pId) {
        return ResponseEntity.ok(service.getOneProgram(pId));
    }


    @GetMapping("user/{id}")
    public ResponseEntity<List<ProgramFilterListDto>> getList(@PathVariable(name = "id") Long hostId,
                                                              @RequestParam(required = false) List<Long> pIds,
                                                              @RequestParam(name = "s", required = false) List<String> statuses) {
        return ResponseEntity.ok(service.getList(hostId, pIds, statuses));
    }

}
