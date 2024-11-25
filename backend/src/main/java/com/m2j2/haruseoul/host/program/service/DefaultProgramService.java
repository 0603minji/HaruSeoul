package com.m2j2.haruseoul.host.program.service;

import com.m2j2.haruseoul.entity.*;
import com.m2j2.haruseoul.host.program.dto.*;
import com.m2j2.haruseoul.host.program.mapper.ProgramMapper;
import com.m2j2.haruseoul.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.*;


@Service
public class DefaultProgramService implements ProgramService {

    private final ProgramRepository programRepository;
    private final CategoryProgramRepository categoryProgramRepository;
    private final RouteRepository routeRepository;
    private final ModelMapper mapper;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final TransportationRepository transportationRepository;
    private final ImageRepository imageRepository;
    private final PublishedProgramRepository publishedProgramRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;


    //  생성자 주입
    public DefaultProgramService(ProgramRepository programRepository, CategoryProgramRepository categoryProgramRepository,
                                 RouteRepository routeRepository, ModelMapper mapper, MemberRepository memberRepository,
                                 CategoryRepository categoryRepository,
                                 TransportationRepository transportationRepository,
                                 ImageRepository imageRepository, PublishedProgramRepository publishedProgramRepository) {
        this.programRepository = programRepository;
        this.categoryProgramRepository = categoryProgramRepository;
        this.routeRepository = routeRepository;
        this.mapper = mapper;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.transportationRepository = transportationRepository;
        this.imageRepository = imageRepository;
        this.publishedProgramRepository = publishedProgramRepository;
    }

    @Override
    @Transactional
    public ProgramResponseDto getList(Long id, List<Long> pIds, List<Long> cIds, List<String> statuses, int pageNum, int cardsPerPage) {

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


        Page<Program> programPage = programRepository.findAllByMid(id, pIds, statuses, pageable);
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
    @Transactional
    public List<ProgramFilterListDto> getList(Long hostId, List<Long> pIds, List<String> statuses) {
        List<Program> programs = programRepository.findAllList(hostId, pIds, statuses);

        return programs.stream()
                .map(program -> mapper.map(program, ProgramFilterListDto.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProgramTitle> getProgramTitles() {
        return programRepository.findAllByOrderByTitle();
    }


    @Override
    @Transactional
    public Program create(ProgramCreateDto programCreateDto, List<MultipartFile> images) {

        Long regMemberId = programCreateDto.getRegMemberId();
        Member regMember = memberRepository.findById(regMemberId).orElse(null);

        Program program = Program.builder()
                .title(programCreateDto.getTitle())
                .detail(programCreateDto.getDetail())
                .member(regMember)
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
        System.out.println("==============================");
        System.out.println(routeCreateDtos);

        //  routeCreateDtos(List)에 담긴 routeCreateDto 하나하나를 Route객체로 변환하고
        //  그 객체들을 리스트 routes에 add하여 누적
        for (RouteCreateDto routeCreateDto : routeCreateDtos) {
            // 이동수단 설정: transportationId가 null이 아닌 경우에만 조회
            Transportation newTransportation = null;
            if (routeCreateDto.getTransportationId() != null) {
                newTransportation = transportationRepository.findById(routeCreateDto.getTransportationId()).orElse(null);
            }

            // Route 엔티티 생성
            Route route = Route.builder()
                    .program(savedProgram)
                    .title(routeCreateDto.getTitle())
                    .address(routeCreateDto.getAddress())
                    .description(routeCreateDto.getDescription())
                    .duration(getLocalTimeByDuration(routeCreateDto.getDuration()))
                    .order(routeCreateDto.getOrder())
                    .startTime(getLocalTimeByHourAndMinute(routeCreateDto.getStartTimeHour(), routeCreateDto.getStartTimeMinute()))
                    .transportation(newTransportation)
                    .transportationDuration(
                            routeCreateDto.getTransportationDuration() != null
                                    ? convertMinutesToLocalTime(routeCreateDto.getTransportationDuration())
                                    : null
                    )
                    .build();

            routes.add(route);
        }

        routeRepository.saveAll(routes);

        // 이미지 저장 로직 추가
        List<Image> imageEntities = new ArrayList<>();
        Integer order = 1;
        if(images != null) {
            for (MultipartFile file : images) {
                if (file != null && !file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir, fileName);
                    try {
                        // 경로가 존재하지 않으면 경로 생성
                        Files.createDirectories(filePath.getParent());
                        // 파일 저장
                        Files.write(filePath, file.getBytes());
                        // 이미지 객체 생성 및 순서 설정
                        Image image = Image.builder()
                                .program(savedProgram)
                                .src("uploads/" + fileName)      // 파일 이름을 src에 저장
                                .order(order++)     // 순서 증가
                                .build();
                        imageEntities.add(image);

                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("이미지 저장 중 오류 발생: " + fileName);
                    }
                }
            }
            imageRepository.saveAll(imageEntities);
        }
        return savedProgram;
    }


    @Override
    @Transactional
    public Program update(ProgramUpdateDto programUpdateDto, List<MultipartFile> images) {

        //  주어진 프로그램 id로 기존 프로그램 조회
        //  null 결과반환 대비해서 Optional 타입
        Program program = programRepository.findByProgramId(programUpdateDto.getProgramId());

        program.setTitle(programUpdateDto.getTitle());
        program.setDetail(programUpdateDto.getDetail());
        program.setLanguage(programUpdateDto.getLanguage());
        program.setStartTime(getLocalTimeByHourAndMinute(programUpdateDto.getStartTimeHour(), programUpdateDto.getStartTimeMinute()));
        program.setEndTime(getLocalTimeByHourAndMinute(programUpdateDto.getEndTimeHour(), programUpdateDto.getEndTimeMinute()));
        program.setGroupSizeMin(programUpdateDto.getGroupSizeMin());
        program.setGroupSizeMax(programUpdateDto.getGroupSizeMax());
        program.setPrice(programUpdateDto.getPrice());
        program.setInclusion(programUpdateDto.getInclusion());
        program.setExclusion(programUpdateDto.getExclusion());
        program.setPackingList(programUpdateDto.getPackingList());
        program.setRequirement(programUpdateDto.getRequirement());
        program.setCaution(programUpdateDto.getCaution());
        program.setStatus(programUpdateDto.getStatus());

        Program updatedProgram = programRepository.save(program);


        Long programId = updatedProgram.getId();
        categoryProgramRepository.deleteByProgramId(programId);
        imageRepository.deleteByProgramId(programId);
        routeRepository.deleteByProgramId(programId);

        //  programUpdateDto에 있는 새로운 카테고리 id 리스트를 categoryIds에 저장
        List<Long> categoryIds = programUpdateDto.getCategoryIds();
        //  CategoryProgram 객체들을 담는 리스트  newCategoryPrograms
        List<CategoryProgram> newCategoryPrograms = new ArrayList<>();

        //  categoryIds(List)에서 category(Long) 하나씩 반복
        for (Long categoryId : categoryIds) {
            //  category 테이블에서 해당 카테고리 id를 조회
            //  null 결과 반환 대비해서  Optional 타입
            Category category = categoryRepository.findByCategoryId(categoryId);

            //  category_program 테이블에 저장하기 위한
            //  CategoryProgram 객체로 빌드 (새로운 카테고리를 함께 빌드)
            CategoryProgram categoryProgram = CategoryProgram.builder()
                    .program(updatedProgram)
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

        List<RouteCreateDto> routeCreateDtos = programUpdateDto.getRoutes();
        System.out.println("=====================================");
        System.out.println(routeCreateDtos);
        //  routeCreateDtos를 변환해서 담을 Route 엔티티가 여러개인 List routes 생성
        ArrayList<Route> routes = new ArrayList<>();

        //  routeCreateDtos(List)에 담긴 routeCreateDto 하나하나를 Route객체로 변환하고
        //  그 객체들을 리스트 routes에 add하여 누적
        for (RouteCreateDto routeCreateDto : routeCreateDtos) {
            //  이동수단
            Transportation newTransportation = null;
            if (routeCreateDto.getTransportationId() != null) {
                newTransportation = transportationRepository.findById(routeCreateDto.getTransportationId()).orElse(null);
            }
            //  이동수단 제외 나머지 속성들
            Route route = Route.builder()
                    .program(updatedProgram)
                    .title(routeCreateDto.getTitle())
                    .address(routeCreateDto.getAddress())
                    .description(routeCreateDto.getDescription())
                    .duration(getLocalTimeByDuration(routeCreateDto.getDuration()))
                    .order(routeCreateDto.getOrder())
                    .startTime(getLocalTimeByHourAndMinute(routeCreateDto.getStartTimeHour(), routeCreateDto.getStartTimeMinute()))
                    .transportation(newTransportation)
                    .transportationDuration(convertMinutesToLocalTime(routeCreateDto.getTransportationDuration())) // String을 LocalTime으로
                    .build();
            routes.add(route);
        }
        routeRepository.saveAll(routes);

        List<Image> imageEntities = new ArrayList<>();
        Integer order = 1;

        if (images != null) { // images가 null이 아닐 때만 처리
            for (MultipartFile file : images) {
                if (file != null && !file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir, fileName);
                    try {
                        Files.createDirectories(filePath.getParent());
                        Files.write(filePath, file.getBytes());

                        Image image = Image.builder()
                                .program(updatedProgram)
                                .src("uploads/" + fileName)
                                .order(order++)
                                .build();
                        imageEntities.add(image);

                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("이미지 저장 중 오류 발생: " + fileName);
                    }
                }
            }
        }

        if (!imageEntities.isEmpty()) {
            imageRepository.saveAll(imageEntities);
        }


        return updatedProgram;
    }


    @Override
    @Transactional
    public ProgramEditDto getProgram(Long id) {
        Program program = programRepository.findById(id).orElse(null);

        List<Route> routes = Optional.ofNullable(routeRepository.findByProgramId(id)).orElse(Collections.emptyList());
        List<String> imageSrcs = Optional.ofNullable(imageRepository.findSrcByProgramId(id)).orElse(Collections.emptyList());
        List<Long> categoryIds = Optional.ofNullable(categoryProgramRepository.findCategoryIdsByProgramId(id)).orElse(Collections.emptyList());

        List<RouteCreateDto> routeDtos = routes.stream()
                .map(route -> {
                    Long transportationId = route.getTransportation() != null ? route.getTransportation().getId() : null;
                    String transportationName = route.getTransportation() != null ? route.getTransportation().getName() : null;


                    return RouteCreateDto.builder()
                            .transportationId(transportationId)
                            .order(route.getOrder())
                            .title(route.getTitle())
                            .address(route.getAddress())
                            .duration(LocalTimeToIntegerConverter(route.getDuration()))
                            .description(route.getDescription())
                            .transportationDuration(
                                    route.getTransportationDuration() != null
                                            ? String.valueOf(route.getTransportationDuration().getMinute())
                                            : "0"
                            )
                            .transportationName(transportationName)
                            .startTimeHour(String.format("%02d", route.getStartTime().getHour()))
                            .startTimeMinute(String.format("%02d", route.getStartTime().getMinute()))
                            .build();
                })
                .toList();


        LocalTime startTime = program.getStartTime();
        LocalTime endTime = program.getEndTime();
        String startTimeHour = String.format("%02d", startTime.getHour());
        String startTimeMinute = String.format("%02d", startTime.getMinute());
        String endTimeHour = String.format("%02d", endTime.getHour());
        String endTimeMinute = String.format("%02d", endTime.getMinute());

        ProgramEditDto programEditDto = ProgramEditDto
                .builder()
                .title(program.getTitle())
                .detail(program.getDetail())
                .language(program.getLanguage())
                .categoryIds(categoryIds)
                .startTimeHour(startTimeHour)
                .startTimeMinute(startTimeMinute)
                .endTimeHour(endTimeHour)
                .endTimeMinute(endTimeMinute)
                .groupSizeMin(program.getGroupSizeMin())
                .groupSizeMax(program.getGroupSizeMax())
                .price(program.getPrice())
                .inclusion(program.getInclusion())
                .exclusion(program.getExclusion())
                .packingList(program.getPackingList())
                .requirement(program.getRequirement())
                .caution(program.getCaution())
                .routes(routeDtos)
                .src(imageSrcs)
                .build();



        return programEditDto;

    }


    public void delete(Long programId) {
        List<Long> allowedStatusId = List.of(3L, 4L);

        if(!publishedProgramRepository.existsByProgramId(programId) ||
        publishedProgramRepository.existsByProgramIdAndStatusIdIn(programId, allowedStatusId)) {

            programRepository.deleteById(programId);
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "공개중인 프로그램이라 삭제할 수 없습니다");
        }
    }

    @Override
    public Program statusCheck(Long pId) {
        Program program = programRepository.findById(pId).orElseThrow(() -> new IllegalArgumentException("프로그램이 존재하지 않습니다."));
        List<PublishedProgram> allUnpaged = publishedProgramRepository.findAllUnpaged(null, null, null, List.of(1L, 2L, 5L, 6L), List.of(pId), null);

        // 개설된 프로그램이 없다면 프로그램 상태를 변경. Published -> Unpublished
        if (allUnpaged.isEmpty()) {
            program.setStatus("Unpublished");
            programRepository.save(program);
        }
        return program;
    }

    @Override
    public Program softDelete(Long pId) {
        Program byId = programRepository.findById(pId).orElseThrow(()->new IllegalArgumentException("프로그램이 존재하지 않습니다."));

        // 삭제 날짜 추가하기
        Date deleteDate = new Date(); // 현재 시간
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(deleteDate);
        calendar.add(Calendar.HOUR, 9); // 9시간 추가
        deleteDate = calendar.getTime();
        byId.setDeleteDate(deleteDate.toInstant());
        programRepository.save(byId);

        return byId;
    }

    private LocalTime getLocalTimeByHourAndMinute(String hour, String minute) {
        int intHour = (hour == null ? 0 : Integer.parseInt(hour));
        int intMinute = (minute == null ? 0 : Integer.parseInt(minute));

        return LocalTime.of(intHour, intMinute);
    }

    private LocalTime getLocalTimeByDuration(Integer duration) {
        if (duration == null) return LocalTime.of(0, 0);

        int hour = duration / 60;
        int minute = duration % 60;
        return LocalTime.of(hour, minute);
    }

    public Integer LocalTimeToIntegerConverter(LocalTime localTime) {
            int hour = localTime.getHour();
            int minute = localTime.getMinute();

            return hour * 60 + minute;

    }
    private LocalTime convertMinutesToLocalTime(String duration) {
        if (duration == null || duration.isEmpty()) {
            return null; // Null 또는 빈 문자열 처리
        }

        try {
            int minutes = Integer.parseInt(duration); // String을 Integer로 변환
            return LocalTime.of(minutes / 60, minutes % 60); // 시간과 분으로 LocalTime 생성
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid duration format: " + duration, e);
        }
    }



}
