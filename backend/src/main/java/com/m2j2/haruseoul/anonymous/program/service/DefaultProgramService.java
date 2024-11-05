package com.m2j2.haruseoul.anonymous.program.service;

import com.m2j2.haruseoul.anonymous.program.dto.ProgramListDto;
import com.m2j2.haruseoul.anonymous.program.dto.ProgramResponseDto;
import com.m2j2.haruseoul.entity.Image;
import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.repository.CategoryRepository;
import com.m2j2.haruseoul.repository.MemberRepository;
import com.m2j2.haruseoul.repository.ProgramRepository;
import com.m2j2.haruseoul.repository.PublishedProgramRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("anonymousProgramService")
public class DefaultProgramService implements ProgramService {

    private final ProgramRepository programRepository;
    private final PublishedProgramRepository publishedProgramRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;


    public DefaultProgramService(CategoryRepository categoryRepository, ProgramRepository programRepository, PublishedProgramRepository publishedProgramRepository, MemberRepository memberRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.programRepository = programRepository;
        this.publishedProgramRepository = publishedProgramRepository;
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ProgramResponseDto getList(List<Long> programIds, List<Long> categoryIds, LocalDate startDate,
                                      LocalDate endDate, Integer minPrice,
                                      Integer maxPrice, Integer groupSizeMax, Integer groupSizeMin,
                                      Integer duration, Integer startTime, String language,
                                      Integer page, Integer pageSize) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        List<Long> filterdProgramIds = publishedProgramRepository.findProgramIdsByDateRange(startDate, endDate);

        if (programIds != null && !programIds.isEmpty()) {
            filterdProgramIds.retainAll(programIds);
        }

        LocalTime morningTime = LocalTime.of(12, 0);  // 오전 12시
        LocalTime afternoonTime = LocalTime.of(18, 0);  // 오후 6시

        Page<Program> programs = programRepository.findProgramsByFilters(
                filterdProgramIds, categoryIds, minPrice, maxPrice, groupSizeMin, groupSizeMax, startTime, morningTime, afternoonTime, language, duration, pageable);

        List<Program> filteredPrograms = programs.getContent();

        if (duration != null) {
            filteredPrograms = filteredPrograms.stream()
                    .filter(p -> Duration.between(p.getStartTime(), p.getEndTime()).toMinutes() <= duration)
                    .toList();
        }

        List<ProgramListDto> programDtos = programs.stream()
                .map(program -> ProgramListDto.builder()
                        .id(program.getId())
                        .title(program.getTitle())
                        .categoryNames(program.getCategoryPrograms().stream()
                                .map(categoryProgram -> categoryProgram.getCategory().getName())
                                .toList())
                        .price(program.getPrice())
                        .rating(program.getRating())
                        .totalRatingCount(program.getReviews().size())
                        .duration(Math.round((Duration.between(program.getStartTime(), program.getEndTime()).toMinutes() / 60.0f) * 10) / 10.0f)
                        .img(program.getImages().stream().findFirst().map(Image::getSrc).orElse(null))
                        .build())
                .toList();

        return ProgramResponseDto.builder()
                .programs(programDtos)
                .totalRowCount((int) programs.getTotalElements())
                .build();

    }
}
