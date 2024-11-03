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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
    public ProgramResponseDto getList(List<Long> pIds, List<Long> cIds, List<String> statuses, int pageNum, int cardsPerPage) {

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
        if (regMember.isPresent() == false) {
            log.error("[ 에러 ] 등록되지 않은 사용자 입니다");
            return null;
        }
        Program program = Program.builder()
                .title(programCreateDto.getTitle())
                .detail(programCreateDto.getDetail())
                .member(regMember.get())
                .language(programCreateDto.getLanguage())
                .startTime(getLocalTime(programCreateDto.getStartTimeHour(), programCreateDto.getStartTimeMinute()))
                .endTime(getLocalTime(programCreateDto.getEndTimeHour(), programCreateDto.getEndTimeMinute()))
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
                    .transportation(newTransportation)
                    .transportationDuration(getLocalTime("00", routeCreateDto.getTransportationDuration()))
                    .build();
            routes.add(route);
        }

        log.info("{}", routes.size());
        routeRepository.saveAll(routes);
        return savedProgram;
    }

    public List<Image> saveImages(Long programId, List<MultipartFile> multipartFiles) {
        // 1. 프로그램이 실재하는 프로그램인지 확인한다.
        Optional<Program> programById = programRepository.findById(programId);
        if (programById.isEmpty()) {
            log.error("[에러] 등록되지 않은 프로그램 입니다", programId);
            return null;
        }
        Program savedProgram = programById.get();

        AtomicInteger index = new AtomicInteger(0);
        List<Image> images = multipartFiles.stream()
                .map(multipartFile -> {
                    try {
                        return Image.builder()
                                .order(index.getAndIncrement())  // AtomicInteger로 인덱스 증가
                                .src(multipartFile.getBytes())
                                .program(savedProgram)
                                .build();
                    } catch (IOException e) {
                        log.error("Error Message: {}", e.getMessage());
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        List<Image> savedImages = imageRepository.saveAll(images);
        log.info("{} 개의 이미지가 정상 저장되었습니다.", savedImages.size());
        return savedImages;
    }

    private LocalTime getLocalTimeByDuration(Integer duration) {
        if (duration == null) return LocalTime.of(0, 0);

        int hour = duration / 60;
        int minute = duration % 60;
        return LocalTime.of(hour, minute);
    }

    /**
     * String 타입의 시간, 분 정보를 받아서 LocalTime 으로 변경해주는 함수.
     * 왜 변경 할까?
     * ㄴ DB 에 저장하는 entity 의 자료형이 LocalTime 이기 때문에 변환한다.
     */
    private LocalTime getLocalTime(String timeHour, String timeMinute) {
        String timeString = timeFormatter(timeHour) + ":" + timeFormatter(timeMinute); // 문자열로 결합
        return LocalTime.parse(timeString); // "HH:mm" 형식이어야 함
    }

    /**
     * 시간이 한 자리 수 일때, 두 자리 수로 변경해주는 함수.
     * 왜 해줘야 해?
     * ㄴ LocalTime 자료형으로 파싱되려면 00:00 형태이어야하기 때문에 시, 분을 각각 두자리로 표현 필요
     */

    private String timeFormatter(String time) { // 0, 5, 10, 15, 23
        if (time == null || time.length() == 0) {
            return "00";
        }
        if (time.length() == 1) {
            return "0" + time;
        }
        return time;
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
