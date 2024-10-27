package com.m2j2.haruseoul.host.program.service;

import com.m2j2.haruseoul.entity.Category;
import com.m2j2.haruseoul.entity.CategoryProgram;
import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.host.program.dto.*;
import com.m2j2.haruseoul.host.program.mapper.ProgramMapper;
import com.m2j2.haruseoul.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultProgramService implements ProgramService {

    private ProgramRepository programRepository;
    private CategoryProgramRepository categoryProgramRepository;
    private RouteRepository routeRepository;
    private final ModelMapper mapper;
    private MemberRepository memberRepository;
    private CategoryRepository categoryRepository;

    //  생성자 주입
    public DefaultProgramService(ProgramRepository programRepository, CategoryProgramRepository categoryProgramRepository,
                                 RouteRepository routeRepository, ModelMapper mapper, MemberRepository memberRepository,
                                 CategoryRepository categoryRepository) {
        this.programRepository = programRepository;
        this.categoryProgramRepository = categoryProgramRepository;
        this.routeRepository = routeRepository;
        this.mapper = mapper;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ProgramResponseDto getList(List<Long> pIds, List<Long> cIds, List<String> statuses) {

        Sort sort = Sort.by("regDate").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);


        List<Long> pIdsFromCategory = null;

        //  전달받은 카테고리 id가 있을때
        if (cIds != null && !cIds.isEmpty()) {
            List<CategoryProgram> categoryPrograms = categoryProgramRepository.findByCategoryIdIn(cIds);
            pIdsFromCategory = categoryPrograms
                    .stream()
                    .map(CategoryProgram::getProgram)
                    .map(Program::getId)
                    .toList();
        }

        if (pIds != null && pIdsFromCategory != null)
            pIds.retainAll(pIdsFromCategory);

        if (pIds == null && pIdsFromCategory != null)
            pIds = pIdsFromCategory;


        Page<Program> programPage = programRepository.findAll(pIds, statuses, pageable);
        List<ProgramListDto> programListDtos = programPage.getContent()
                .stream()
                .map(ProgramMapper::mapToDto)
                .toList();
        long totalRowCount = programPage.getTotalElements();
        long totalPageCount = programPage.getTotalPages();
        ProgramResponseDto programResponseDto = ProgramResponseDto.builder()
                .programs(programListDtos)
                .totalRowCount(totalRowCount)
                .totalPageCount(totalPageCount)
                .build();

        return programResponseDto;
    }

    public List<ProgramTitle> getProgramTitles() {
        return programRepository.findAllByOrderByTitle();
    }


    @Override
    @Transactional
    public Program create(ProgramCreateDto programCreateDto) {
        //Program program = mapper.map(programCreateDto, Program.class);
        Optional<Member> regMember = memberRepository.findById(programCreateDto.getRegMemberId());

        Program program = Program.builder()
                .title(programCreateDto.getTitle())
                .detail(programCreateDto.getDetail())
                .member(regMember.get())
                .language(programCreateDto.getLanguage())
                .startTime(LocalTime.parse(programCreateDto.getStartTime()))
                .endTime(LocalTime.parse(programCreateDto.getEndTime()))
                .groupSizeMin(programCreateDto.getGroupSizeMin())
                .groupSizeMax(programCreateDto.getGroupSizeMax())
                .price(programCreateDto.getPrice())
                .inclusion(programCreateDto.getInclusion())
                .exclusion(programCreateDto.getExclusion())
                .packingList(programCreateDto.getPackingList())
                .requirement(programCreateDto.getRequirement())
                .caution(programCreateDto.getCaution())
                .build();
        //  ** category, route, image 빌드 실패 **

        Program savedProgram = programRepository.save(program);


        List<Long> categoryIds = programCreateDto.getCategoryIds();
        List<CategoryProgram> categoryPrograms = new ArrayList<>();

        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId).get();
            CategoryProgram categoryProgram = CategoryProgram.builder()
                    .program(savedProgram)
                    .category(category)
                    .build();
            categoryPrograms.add(categoryProgram);
        }
        categoryProgramRepository.saveAll(categoryPrograms);

        return savedProgram;
    }

    @Override
    @Transactional
    public Program update(ProgramUpdateDto programUpdateDto) {

        Program oldProgram = programRepository.findById(programUpdateDto.getProgramId()).get();

        oldProgram.setTitle(programUpdateDto.getTitle());
        oldProgram.setDetail(programUpdateDto.getDetail());
        oldProgram.setLanguage(programUpdateDto.getLanguage());
        oldProgram.setStartTime(LocalTime.parse(programUpdateDto.getStartTime()));
        oldProgram.setEndTime(LocalTime.parse(programUpdateDto.getEndTime()));
        oldProgram.setGroupSizeMin(programUpdateDto.getGroupSizeMin());
        oldProgram.setGroupSizeMax(programUpdateDto.getGroupSizeMax());
        oldProgram.setPrice(programUpdateDto.getPrice());
        oldProgram.setInclusion(programUpdateDto.getInclusion());
        oldProgram.setExclusion(programUpdateDto.getExclusion());
        oldProgram.setPackingList(programUpdateDto.getPackingList());
        oldProgram.setRequirement(programUpdateDto.getRequirement());
        oldProgram.setCaution(programUpdateDto.getCaution());

        Program newProgram = programRepository.save(oldProgram);

        Long programId = newProgram.getId();

        categoryProgramRepository.deleteByProgramId(programId);

        List<Long> categoryIds = programUpdateDto.getCategoryIds();

        List<CategoryProgram> newCategoryPrograms = new ArrayList<>();

        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId).get();
            CategoryProgram categoryProgram = CategoryProgram.builder()
                    .program(newProgram)
                    .category(category)
                    .build();
            newCategoryPrograms.add(categoryProgram);
        }
        categoryProgramRepository.saveAll(newCategoryPrograms);

        return newProgram;
    }

    public void delete(Long programId) {
        programRepository.deleteById(programId);
    }


}
