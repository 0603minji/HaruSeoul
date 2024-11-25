package com.m2j2.haruseoul.anonymous.program.service;

import com.m2j2.haruseoul.anonymous.program.dto.*;
import com.m2j2.haruseoul.entity.Image;
import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.repository.ProgramRepository;
import com.m2j2.haruseoul.repository.PublishedProgramRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service("anonymousProgramService")
public class DefaultProgramService implements ProgramService {

    private final ProgramRepository programRepository;
    private final PublishedProgramRepository publishedProgramRepository;
    private final CategoryProgramService categoryProgramService;
    private final ProgramDetailImageService programDetailImageService;
    private final ProgramDetailService programDetailService;
    private final ProgramDetailMemberService programDetailMemberService;
    private final ProgramDetailReviewService programDetailReviewService;
    private final ProgramDetailRouteService programDetailRouteService;


    public DefaultProgramService(CategoryProgramService categoryProgramService, ProgramRepository programRepository, PublishedProgramRepository publishedProgramRepository, ProgramDetailImageService programDetailImageService, ProgramDetailService programDetailService, ProgramDetailMemberService programDetailMemberService, ProgramDetailReviewService programDetailReviewService, ProgramDetailRouteService programDetailRouteService) {
        this.categoryProgramService = categoryProgramService;
        this.programRepository = programRepository;
        this.publishedProgramRepository = publishedProgramRepository;
        this.programDetailImageService = programDetailImageService;
        this.programDetailService = programDetailService;
        this.programDetailMemberService = programDetailMemberService;
        this.programDetailReviewService = programDetailReviewService;
        this.programDetailRouteService = programDetailRouteService;
    }

    @Override
    @Transactional
    public ProgramResponseDto getList(List<Long> programIds, List<Long> categoryIds,String title, LocalDate startDate,
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
                filterdProgramIds, categoryIds,title, minPrice, maxPrice, groupSizeMin, groupSizeMax, startTime, morningTime, afternoonTime, language, duration, pageable);

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

    @Override
    @Transactional
    public ProgramDetailResponseDto getList(Long id) {
        ProgramDetailProgramDto programDetailProgramDto = programDetailService.getDetail(id);
        ProgramDetailCategoryDto programDetailCategoryDto = categoryProgramService.getCategoryNames(id);
        ProgramDetailImageDto programDetailImageDto = programDetailImageService.getSrc(id);
        ProgramDetailMemberDto programDetailMemberDto = programDetailMemberService.getName(id);
        ProgramDetailReviewDto programDetailReviewDto = programDetailReviewService.getList(id);
        List<ProgramDetailRouteDto> programDetailRouteDto = programDetailRouteService.getRoutes(id);

        ProgramDetailResponseDto programDetailResponseDto = ProgramDetailResponseDto
                .builder()
                .programDetailProgramDto(programDetailProgramDto)
                .programDetailCategoryDto(programDetailCategoryDto)
                .programDetailImageDto(programDetailImageDto)
                .programDetailMemberDto(programDetailMemberDto)
                .programDetailReviewDto(programDetailReviewDto)
                .programDetailRouteDto(programDetailRouteDto)
                .build();
        return programDetailResponseDto;
    }
}
