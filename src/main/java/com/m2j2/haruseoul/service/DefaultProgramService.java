package com.m2j2.haruseoul.service;

import com.m2j2.haruseoul.dto.ProgramDto;
import com.m2j2.haruseoul.dto.ProgramResponseDto;
import com.m2j2.haruseoul.entity.CategoryProgram;
import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.mapper.ProgramMapper;
import com.m2j2.haruseoul.repository.CategoryProgramRepository;
import com.m2j2.haruseoul.repository.ProgramRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultProgramService implements ProgramService {
    
    private ProgramRepository programRepository;
    private CategoryProgramRepository categoryProgramRepository;

    //  생성자 주입
    public DefaultProgramService(ProgramRepository programRepository, CategoryProgramRepository categoryProgramRepository) {
        this.programRepository = programRepository;
        this.categoryProgramRepository = categoryProgramRepository;
    }

    @Override
    public ProgramResponseDto getList(List<Long> programIds, List<Long> categoryIds, List<String> statuses) {
        
        Sort sort = Sort.by("regDate").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);
        List<CategoryProgram> categoryPrograms = categoryProgramRepository.findByCategoryIdIn(categoryIds);
        List<Long> pIds = categoryPrograms
                .stream()
                .map(CategoryProgram::getId)
                .toList();

        Page<Program> programPage = programRepository.findAll(pIds, statuses, pageable);
        List<ProgramDto> programDtos= programPage.getContent()
                .stream()
                .map(ProgramMapper::mapToDto)
                .toList();
        long totalRowCount = programPage.getTotalElements();
        long totalPageCount = programPage.getTotalPages();
        ProgramResponseDto programResponseDto = ProgramResponseDto.builder()
                .programs(programDtos)
                .totalRowCount(totalRowCount)
                .totalPageCount(totalPageCount)
                .build();

        return programResponseDto;
    }
}
