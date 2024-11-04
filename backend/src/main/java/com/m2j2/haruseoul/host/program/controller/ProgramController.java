package com.m2j2.haruseoul.host.program.controller;

import com.m2j2.haruseoul.anonymous.service.DefaultCategoryService;
import com.m2j2.haruseoul.entity.Category;
import com.m2j2.haruseoul.entity.Image;
import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.host.program.dto.*;
import com.m2j2.haruseoul.host.program.service.DefaultProgramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
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
            @RequestParam(name = "s", required = false) List<String> statuses,
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(name = "cardsPerPage", defaultValue = "6") int cardsPerPage)
    {

        ProgramResponseDto programResponseDto = service.getList(pIds, cIds, statuses, pageNum, cardsPerPage);

        return ResponseEntity.ok(programResponseDto);
    }

    @PostMapping
    public ResponseEntity<Program> create(@RequestBody ProgramCreateDto programCreateDto) {
        return ResponseEntity.ok(service.create(programCreateDto));
    }

//    @PostMapping("/images")
//    public ResponseEntity<List<Image>> saveImage(
//            @RequestPart("programId") Long programId,
//            @RequestPart("images") List<MultipartFile> images
//    ) {
//        log.info("programId: {}", programId);
//        log.info("images size: {}", images.size());
//        return ResponseEntity.ok(service.saveImages(programId, images));
//    }

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
