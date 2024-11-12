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
    private ImageRepository imageRepository;

    //  생성자 주입
    public DefaultProgramService(ProgramRepository programRepository, CategoryProgramRepository categoryProgramRepository,
                                 RouteRepository routeRepository, ModelMapper mapper, MemberRepository memberRepository,
                                 CategoryRepository categoryRepository,
                                 TransportationRepository transportationRepository,
                                 ImageRepository imageRepository) {
        this.programRepository = programRepository;
        this.categoryProgramRepository = categoryProgramRepository;
        this.routeRepository = routeRepository;
        this.mapper = mapper;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.transportationRepository = transportationRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    @Transactional
    public ProgramResponseDto getList(Long id,List<Long> pIds, List<Long> cIds, List<String> statuses, int pageNum, int cardsPerPage) {

        Sort sort = Sort.by("regDate").descending();
        Pageable pageable = PageRequest.of(pageNum - 1, cardsPerPage, sort);


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


        Page<Program> programPage = programRepository.findAllByMid(id,pIds, statuses, pageable);
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

    @Override
    public List<ProgramFilterListDto> getList(Long hostId, List<Long> pIds, List<String> statuses) {
        List<Program> programs = programRepository.findAllList(hostId, pIds, statuses);

        return programs.stream()
                .map(program -> mapper.map(program, ProgramFilterListDto.class))
                .toList();
    }

    public List<ProgramTitle> getProgramTitles() {
        return programRepository.findAllByOrderByTitle();
    }


    @Override
    @Transactional
    public Program create(ProgramCreateDto programCreateDto) {
        //Program program = mapper.map(programCreateDto, Program.class);
        Optional<Member> regMember = memberRepository.findById(programCreateDto.getRegMemberId());
        if (regMember.isPresent() == false) {
            log.error("[ 에러 ] 등록되지 않은 사용자 입니다");
            return null;
        }
        Program program = Program.builder()
                .title(programCreateDto.getTitle())
                .detail(programCreateDto.getDetail())
                .member(regMember.get())
                .language(programCreateDto.getLanguage())
                .startTime(getLocalTimeByHourAndMinute(programCreateDto.getStartTimeHour(), programCreateDto.getStartTimeMinute()))
                .endTime(getLocalTimeByHourAndMinute(programCreateDto.getEndTimeHour(), programCreateDto.getEndTimeMinute()))
                .groupSizeMin(programCreateDto.getGroupSizeMin())
                .groupSizeMax(programCreateDto.getGroupSizeMax())
                .price(programCreateDto.getPrice())
                .inclusion(programCreateDto.getInclusion())
                .exclusion(programCreateDto.getExclusion())
                .packingList(programCreateDto.getPackingList())
                .requirement(programCreateDto.getRequirement())
                .caution(programCreateDto.getCaution())
                .status(programCreateDto.getStatus())
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
        for (RouteCreateDto routeCreateDto : routeCreateDtos) {
            //  이동수단
            Transportation newTransportation = transportationRepository.findById(routeCreateDto.getTransportationId()).orElse(null);
            //  이동수단 제외 나머지 속성들
            Route route = Route.builder()
                    .program(savedProgram)
                    .title(routeCreateDto.getTitle())
                    .address(routeCreateDto.getAddress())
                    .description(routeCreateDto.getDescription())
                    .duration(getLocalTimeByDuration(routeCreateDto.getDuration()))
                    .order(routeCreateDto.getOrder())
                    .startTime(getLocalTimeByHourAndMinute(routeCreateDto.getStartTimeHour(), routeCreateDto.getStartTimeMinute()))
                    .transportation(newTransportation)
                    .transportationDuration(getLocalTimeByHourAndMinute("00", routeCreateDto.getTransportationDuration()))
                    .build();
            routes.add(route);
        }

        log.info("{}", routes.size());
        routeRepository.saveAll(routes);
        return savedProgram;
    }

    private LocalTime getLocalTimeByHourAndMinute(String hour, String minute){
        int intHour = (hour ==  null ? 0 : Integer.parseInt(hour));
        int intMinute = (minute == null ? 0 : Integer.parseInt(minute));

        return LocalTime.of(intHour, intMinute);
    }

    private LocalTime getLocalTimeByDuration(Integer duration) {
        if (duration == null) return LocalTime.of(0, 0);

        int hour = duration / 60;
        int minute = duration % 60;
        return LocalTime.of(hour, minute);
    }
    //  ====== 호스트 프로그램 수정 메서드 ==============================
    @Override
    @Transactional
    public Program update(ProgramUpdateDto programUpdateDto) {

        //  주어진 프로그램 id로 기존 프로그램 조회
        //  null 결과반환 대비해서 Optional 타입
        Optional<Program> programOptional = programRepository.findById(programUpdateDto.getProgramId());

        //  조회 결과 null인 경우 예외 처리
        if(!programOptional.isPresent()) {
            log.error("{} is not found", programUpdateDto.getProgramId());
            return null;
        }

        //  조회 결과 null이 아닌 경우 조회결과를  Program 객체에 저장
        Program oldProgram = programOptional.get();
        
        //  Program 객체의 각 필드를 ProgramUpdateDto의 값으로 업데이트
        oldProgram.setTitle(programUpdateDto.getTitle());
        oldProgram.setDetail(programUpdateDto.getDetail());
        oldProgram.setLanguage(programUpdateDto.getLanguage());
        oldProgram.setStartTime(getLocalTimeByHourAndMinute(programUpdateDto.getStartTimeHour(), programUpdateDto.getStartTimeMinute()));
        oldProgram.setEndTime(getLocalTimeByHourAndMinute(programUpdateDto.getEndTimeHour(), programUpdateDto.getEndTimeMinute()));
        oldProgram.setGroupSizeMin(programUpdateDto.getGroupSizeMin());
        oldProgram.setGroupSizeMax(programUpdateDto.getGroupSizeMax());
        oldProgram.setPrice(programUpdateDto.getPrice());
        oldProgram.setInclusion(programUpdateDto.getInclusion());
        oldProgram.setExclusion(programUpdateDto.getExclusion());
        oldProgram.setPackingList(programUpdateDto.getPackingList());
        oldProgram.setRequirement(programUpdateDto.getRequirement());
        oldProgram.setCaution(programUpdateDto.getCaution());
        oldProgram.setStatus(programUpdateDto.getStatus());

        //  Program 객체를 db에 저장하는 save 메서드 호출
        Program newProgram = programRepository.save(oldProgram);

        //  카테고리(category) 업데이트
        //  카테고리 db가 따로 존재하므로 따로 업데이트 필요
        Long programId = newProgram.getId();

        //  category_program  테이블에 기존 데이터를 삭제
        categoryProgramRepository.deleteByProgramId(programId);

        //  programUpdateDto에 있는 새로운 카테고리 id 리스트를 categoryIds에 저장
        List<Long> categoryIds = programUpdateDto.getCategoryIds();

        //  CategoryProgram 객체들을 담는 리스트  newCategoryPrograms
        List<CategoryProgram> newCategoryPrograms = new ArrayList<>();

        //  categoryIds(List)에서 category(Long) 하나씩 반복
        for (Long categoryId : categoryIds) {
            //  category 테이블에서 해당 카테고리 id를 조회
            //  null 결과 반환 대비해서  Optional 타입
            Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
            //  category 테이블에 존재하지 않는 카테고리  id인 경우
            if(!categoryOptional.isPresent()) {
                log.error("{} is not found", categoryId);
                continue;
            }
            //  category 테이블에 존재하는 카테고리 id인 경우
            //  카테고리 id에 해당하는 category 객체를 저장
            Category category = categoryOptional.get();

            //  category_program 테이블에 저장하기 위한
            //  CategoryProgram 객체로 빌드 (새로운 카테고리를 함께 빌드)
            CategoryProgram categoryProgram = CategoryProgram.builder()
                    .program(newProgram)
                    .category(category)
                    .build();

            //  CategoryProgram 객체들을 담는 리스트  newCategoryPrograms
            //  newCategoryPrograms 리스트에 카테고리 최대 2개에 대한
            //  각각의 CategoryProgram 객체들을 누적받는 리스트
            newCategoryPrograms.add(categoryProgram);
        }
        
        //  CategoryProgram 객체들을 누적받은 리스트를
        //  category_program 테이블에 저장
        categoryProgramRepository.saveAll(newCategoryPrograms);

        //  todo: 코스(route) 업데이트
        //  todo: 이미지(image) 업데이트
        return newProgram;
    }


    public void delete(Long programId) {
        programRepository.deleteById(programId);
    }

    public ProgramListDto getOneProgram(Long pId) {
        //  프로그램 id로 Program 엔티티를 조회
        //  없는 경우(null)를 위해 Optional 객체에 저장
        Optional<Program> programOptional = programRepository.findById(pId);
        //  programOptional이 isEmpty인 경우
        //  null을 반환하여 메서드 종료
        if(!programOptional.isPresent()) return null;
        //  programOptional이 isEmpty가 아닌 경우
        //  get 메서드로 Program 객체를 추출
        Program program = programOptional.get();
        
        //  routeRepository를 통해 Program(프로그램 id)에 속한 Route 객체 리스트를 조회
        List<Route> routes = routeRepository.findByProgramId(pId);
        //  조회한 Program 객체와 Route 리스트를 ProgramListDto로 변환
        ProgramListDto programListDto = ProgramMapper.mapToDto(program, routes);

        return programListDto;
    }
}
