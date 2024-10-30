package com.m2j2.haruseoul.host.program.service;

import com.m2j2.haruseoul.entity.*;
import com.m2j2.haruseoul.host.program.dto.*;
import com.m2j2.haruseoul.host.program.mapper.ProgramMapper;
import com.m2j2.haruseoul.repository.*;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class DefaultProgramService implements ProgramService {

    private ProgramRepository programRepository;
    private CategoryProgramRepository categoryProgramRepository;
    private RouteRepository routeRepository;
    private final ModelMapper mapper;
    private MemberRepository memberRepository;
    private CategoryRepository categoryRepository;
    private TransportationRepository transportationRepository;

    //  생성자 주입
    public DefaultProgramService(ProgramRepository programRepository, CategoryProgramRepository categoryProgramRepository,
                                 RouteRepository routeRepository, ModelMapper mapper, MemberRepository memberRepository,
                                 CategoryRepository categoryRepository,
                                 TransportationRepository transportationRepository) {
        this.programRepository = programRepository;
        this.categoryProgramRepository = categoryProgramRepository;
        this.routeRepository = routeRepository;
        this.mapper = mapper;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.transportationRepository = transportationRepository;
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
        if(regMember.isPresent() == false){
            log.error("[ 에러 ] 등록되지 않은 사용자 입니다");
            return null;
        }

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

        //  programCreateDto에 입력받은 routes 필드 데이터(List<RouteCreateDto>)를 routeCreateDto 객체에 저장
        List<RouteCreateDto> routeCreateDtos = programCreateDto.getRoutes();
        //  routeCreateDtos를 변환해서 담을 Route 엔티티가 여러개인 List routes 생성
        ArrayList<Route> routes = new ArrayList<>();

        //  routeCreateDtos(List)에 담긴 routeCreateDto 하나하나를 Route객체로 변환하고
        //  그 객체들을 리스트 routes에 add하여 누적
        for(RouteCreateDto routeCreateDto : routeCreateDtos){
            //  이동수단
            Transportation newTransportation = transportationRepository.findById(routeCreateDto.getTransportationId()).orElse(null);
            //  이동수단 제외 나머지 속성들
            Route route = Route.builder()
                    .program(savedProgram)
                    .title(routeCreateDto.getTitle())
                    .address(routeCreateDto.getAddress())
                    .description(routeCreateDto.getDescription())
                    .duration(routeCreateDto.getDuration())
                    .order(routeCreateDto.getOrder())
                    .transportation(newTransportation)
                    .transportationDuration(routeCreateDto.getTransportationDuration())
                    .build();
            routes.add(route);
        }

        log.info("{}",routes.size());
        routeRepository.saveAll(routes);

        return savedProgram;

        /**
         * todo
         *  1. category_program 테이블에 새로운 프로그램에 대한 category_id 저장
         *  2. image 테이블에 저장
         */
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
