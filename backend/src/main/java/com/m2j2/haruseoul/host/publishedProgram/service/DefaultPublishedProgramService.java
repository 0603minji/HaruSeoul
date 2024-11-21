package com.m2j2.haruseoul.host.publishedProgram.service;

import com.m2j2.haruseoul.entity.*;
import com.m2j2.haruseoul.host.program.dto.ProgramFilterListDto;
import com.m2j2.haruseoul.host.publishedProgram.dto.*;
import com.m2j2.haruseoul.repository.ProgramRepository;
import com.m2j2.haruseoul.repository.PublishedProgramRepository;
import com.m2j2.haruseoul.repository.StatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class DefaultPublishedProgramService implements PublishedProgramService {

    ProgramRepository programRepository;
    PublishedProgramRepository publishedProgramRepository;
    StatusRepository statusRepository;
    ModelMapper modelMapper;

    public DefaultPublishedProgramService(ProgramRepository programRepository,
                                          PublishedProgramRepository publishedProgramRepository,
                                          StatusRepository statusRepository,
                                          ModelMapper modelMapper) {
        this.programRepository = programRepository;
        this.publishedProgramRepository = publishedProgramRepository;
        this.statusRepository = statusRepository;
        this.modelMapper = modelMapper;
    }

    final long STATUS_ON_GOING = 1L;
    final long STATUS_URGENT = 2L;
    final long STATUS_FINISHED = 3L;
    final long STATUS_CANCELED = 4L;
    final long STATUS_WAIT_CONFIRM = 5L;
    final long STATUS_CONFIRMED = 6L;

    @Override
    public PublishedProgramResponseDto getList(List<Long> memberIds, List<LocalDate> dates, List<Long> statusIds, List<Long> programIds,
                                               Integer page, Integer pageSize, String sortBy, String order) {
        Sort sort = order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        LocalDate start = dates == null ? null : dates.getFirst();
        LocalDate end = dates == null ? null : dates.getLast();

        // Page<pp>
        Page<PublishedProgram> publishedProgramPage = publishedProgramRepository
                .findAll(memberIds, start, end, statusIds, programIds, pageable);

        // List<ppListDto>----------------------------------------------------------------------------------------------
        Converter<List<Reservation>, List<Long>> reservationsToReservationIdsConverter = ctx -> ctx.getSource().stream()
                .filter(rv -> rv.getDeleteDate() == null || (rv.getCancelMethod()!=null && rv.getCancelMethod() == 3)) // reservation 중 게스트가 자의로 취소, 호스트가 kick한 것 제외하되 폐지로 취소된 예약은 포함
                .map(Reservation::getId)
                .toList();

        Converter<List<Reservation>, List<String>> reservationsToProfileImgSrcsConverter = ctx -> ctx.getSource().stream()
                .filter(rv -> rv.getDeleteDate() == null || (rv.getCancelMethod()!=null && rv.getCancelMethod() == 3)) // reservation 중 게스트가 자의로 취소, 호스트가 kick한 것 제외하되 폐지로 취소된 예약은 포함
                .map(Reservation::getMember)
                .map(Member::getProfileImgSrc)
                .toList();

        modelMapper.typeMap(PublishedProgram.class, PublishedProgramListDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getProgram().getGroupSizeMax(), PublishedProgramListDto::setGroupSizeMax);
                    mapper.map(src -> src.getProgram().getGroupSizeMin(), PublishedProgramListDto::setGroupSizeMin);
                    mapper
                            .using(reservationsToReservationIdsConverter)
                            .map(PublishedProgram::getReservations, PublishedProgramListDto::setReservationIds);
                    mapper.using(reservationsToProfileImgSrcsConverter)
                                    .map(PublishedProgram::getReservations, PublishedProgramListDto::setGuestProfileImgSrcs);
                    mapper.map(
                            src -> Optional.ofNullable(src.getProgram().getImages())
                                    .map(images -> images.stream()
                                            .sorted(Comparator.comparing(Image::getOrder))
                                            .toList())
                                    .orElse(Collections.emptyList())
                            , PublishedProgramListDto::setImages
                    );
                });

        List<PublishedProgramListDto> publishedProgramListDtos = publishedProgramPage.stream()
                .map(publishedProgram -> modelMapper.map(publishedProgram, PublishedProgramListDto.class))
                .toList();

        //--------------------------------------------------------------------------------------------------------------

        long totalCount = publishedProgramPage.getTotalElements();
        long totalPages = publishedProgramPage.getTotalPages();
        int currentPageRowCount = publishedProgramPage.getNumberOfElements();
        boolean hasNextPage = publishedProgramPage.hasNext();
        boolean hasPreviousPage = publishedProgramPage.hasPrevious();

        // pager에 표시할 목록. 1~5, 6~10...
        int offset = (page - 1) % 5;
        int startNUm = page - offset;
        List<Long> pages = IntStream.range(startNUm, startNUm + 5)
                .boxed()
                .map(Long::valueOf)
                .toList();

        return PublishedProgramResponseDto.builder()
                .publishedPrograms(publishedProgramListDtos)
                .pages(pages)
                .startNum(startNUm)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .currentPageRowCount(currentPageRowCount)
                .hasNextPage(hasNextPage)
                .hasPreviousPage(hasPreviousPage)
                .build();
    }

    @Override
    public PublishedProgramResponseDto getList(List<Long> memberIds, List<LocalDate> dates, List<Long> statusIds, List<Long> programIds,
                                               String sortBy, String order) {
        Sort sort = order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        LocalDate start = dates == null ? null : dates.getFirst();
        LocalDate end = dates == null ? null : dates.getLast();

        List<PublishedProgram> allUnpaged = publishedProgramRepository.findAllUnpaged(memberIds, start, end, statusIds, programIds, sort);

        // List<ppListDto>----------------------------------------------------------------------------------------------
        modelMapper.typeMap(PublishedProgram.class, PublishedProgramListDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getProgram().getGroupSizeMax(), PublishedProgramListDto::setGroupSizeMax);
                    mapper.map(src -> src.getProgram().getGroupSizeMin(), PublishedProgramListDto::setGroupSizeMin);
                    mapper.map(
                            src -> Optional.ofNullable(src.getProgram().getImages())
                                    .map(images -> images.stream()
                                            .sorted(Comparator.comparing(Image::getOrder))
                                            .toList())
                                    .orElse(Collections.emptyList()),
                            PublishedProgramListDto::setImages
                    );
                });

        List<PublishedProgramListDto> publishedProgramListDtos = allUnpaged.stream()
                .map(publishedProgram -> modelMapper.map(publishedProgram, PublishedProgramListDto.class))
                .toList();

        return PublishedProgramResponseDto.builder()
                .publishedPrograms(publishedProgramListDtos)
                .build();
    }

    @Override
    public PublishedProgramResponseDto getList(List<Long> memberIds, List<LocalDate> dates, List<Long> statusIds, List<Long> programIds,
                                               Integer page, Integer pageSize, String sortBy, String order, String tab) {
        // tab : null
        if (tab == null)
            return getList(memberIds, dates, statusIds, programIds, page, pageSize, sortBy, order);

        /*
            선택된 탭 내에서 유효한 옵션 중 체크된 것들로만 쿼리
            ex. 예정된 탭에서 유효한 옵션: 1,2,5,6. 체크된 옵션:2,3,4 => 체크된 옵션 중 3,4는 유효하지 않으므로 유효한 2옵션으로만 쿼리
        */
        if (statusIds == null)
            statusIds = new ArrayList<>();

        // tab : todo(예정된 예약) s=1,2,5,6일 때
        switch (tab) {
            case "todo" -> {
                // 체크된 옵션과 유효한 옵션의 교집합 구하기
                List<Long> retained = new ArrayList<>(Arrays.asList(1L, 2L, 5L, 6L));
                retained.retainAll(statusIds);

                // 겹치는게 있으면 그걸로 쿼리
                if (!retained.isEmpty())
                    return getList(memberIds, dates, retained, programIds, page, pageSize, sortBy, order);
                // 겹치는게 없으면 모든 유효한 옵션으로 쿼리
                return getList(memberIds, dates, List.of(1L, 2L, 5L, 6L), programIds, page, pageSize, sortBy, order);
            }


            // tab : finished(지난 예약) s=3
            case "finished" -> {
                // 체크된 옵션과 유효한 옵션의 교집합 구하기
                List<Long> retained = new ArrayList<>(Arrays.asList(3L));
                retained.retainAll(statusIds);

                // 겹치는게 있으면 그걸로 쿼리
                if (!retained.isEmpty())
                    return getList(memberIds, dates, retained, programIds, page, pageSize, sortBy, order);
                // 겹치는게 없으면 모든 유효한 옵션으로 쿼리
                return getList(memberIds, dates, List.of(3L), programIds, page, pageSize, sortBy, order);
            }


            // tab : canceled(취소된 예약) s=4
            case "canceled" -> {
                // 체크된 옵션과 유효한 옵션의 교집합 구하기
                List<Long> retained = new ArrayList<>(Arrays.asList(4L));
                retained.retainAll(statusIds);

                // 겹치는게 있으면 그걸로 쿼리
                if (!retained.isEmpty())
                    return getList(memberIds, dates, retained, programIds, page, pageSize, sortBy, order);
                // 겹치는게 없으면 모든 유효한 옵션으로 쿼리
                return getList(memberIds, dates, List.of(4L), programIds, page, pageSize, sortBy, order);
            }
        }

        // 그 밖의 유효하지 않은 tab값들은 무시
        return getList(memberIds, dates, statusIds, programIds, page, pageSize, sortBy, order);
    }


    @Override
    public OnGoingPublishedProgramListDto findByProgramId(Long pId) {

        // 특정 프로그램 ID와 상태에 해당하는 프로그램 목록을 조회
        List<PublishedProgram> onGoingPrograms = publishedProgramRepository.findByProgramId(pId);

        // 조회된 각 프로그램을 PublishedProgramListDto 객체로 매핑하여 리스트 생성
        List<PublishedProgramListDto> programListDtos = onGoingPrograms.stream()
                .map(program -> PublishedProgramListDto.builder()
                        .id(program.getId())
                        .regDate(program.getRegDate())
                        .programId(program.getProgram().getId())
                        .programTitle(program.getProgram().getTitle())
                        .groupSizeMax(program.getProgram().getGroupSizeMax())
                        .groupSizeMin(program.getProgram().getGroupSizeMin())
                        .groupSizeCurrent(program.getGroupSizeCurrent())
                        .date(program.getDate())
                        .statusName(program.getStatus().getName())
                        .build())
                .toList();

        // 매핑된 DTO 리스트를 OnGoingPublishedProgramListDto 객체에 설정하여 반환
        return OnGoingPublishedProgramListDto.builder()
                .onGoingPrograms(programListDtos)
                .build();
    }

    @Override
    public PublishedProgramListDto getById(Long id) {
        PublishedProgram pp = publishedProgramRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PublishedProgram not found with ID: " + id));

        // List<ppListDto>----------------------------------------------------------------------------------------------
        Converter<List<Reservation>, List<Long>> reservationsToReservationIdsConverter = ctx -> ctx.getSource().stream()
                .filter(rv -> rv.getDeleteDate() == null || (rv.getCancelMethod()!=null && rv.getCancelMethod() == 3)) // reservation 중 게스트가 자의로 취소, 호스트가 kick한 것 제외하되 폐지로 취소된 예약은 포함
                .map(Reservation::getId)
                .toList();

        Converter<List<Reservation>, List<String>> reservationsToProfileImgSrcsConverter = ctx -> ctx.getSource().stream()
                .filter(rv -> rv.getDeleteDate() == null || (rv.getCancelMethod()!=null && rv.getCancelMethod() == 3)) // reservation 중 게스트가 자의로 취소, 호스트가 kick한 것 제외하되 폐지로 취소된 예약은 포함
                .map(Reservation::getMember)
                .map(Member::getProfileImgSrc)
                .toList();

        modelMapper.typeMap(PublishedProgram.class, PublishedProgramListDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getProgram().getGroupSizeMax(), PublishedProgramListDto::setGroupSizeMax);
                    mapper.map(src -> src.getProgram().getGroupSizeMin(), PublishedProgramListDto::setGroupSizeMin);
                    mapper
                            .using(reservationsToReservationIdsConverter)
                            .map(PublishedProgram::getReservations, PublishedProgramListDto::setReservationIds);
                    mapper.using(reservationsToProfileImgSrcsConverter)
                            .map(PublishedProgram::getReservations, PublishedProgramListDto::setGuestProfileImgSrcs);
                    mapper.map(
                            src -> Optional.ofNullable(src.getProgram().getImages())
                                    .map(images -> images.stream()
                                            .sorted(Comparator.comparing(Image::getOrder))
                                            .toList())
                                    .orElse(Collections.emptyList())
                            , PublishedProgramListDto::setImages
                    );
                });

        return modelMapper.map(pp, PublishedProgramListDto.class);
    }

    @Override
    public PublishedProgramProgramFilterResponseDto getProgramFilterList(Long hostId) {
        List<Long> pIds = publishedProgramRepository.findDistinctProgramIdsByHostId(hostId);
        List<Program> programs = programRepository.findAllList(hostId, pIds, null);
        // program -> programFilterListDto
        List<ProgramFilterListDto> programFilterListDtos = programs.stream()
                .map(program -> modelMapper.map(program, ProgramFilterListDto.class))
                .toList();

        return PublishedProgramProgramFilterResponseDto.builder()
                .programFilterListDtos(programFilterListDtos)
                .build();
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (publishedProgramRepository.existsById(id)) {
            publishedProgramRepository.deleteById(id);
            return true;
        } else
            return false;
    }

    @Override
    @Transactional
    public PublishedProgramCreatedDto create(PublishedProgramCreateDto publishedProgramCreateDto) {
        // <editor-fold desc="입력받은 진행일 유효성 검사코드">
        /* 애초에 프론트 달력ui에서 유효하지 않은 날짜는 선택불가능하도록 처리하겠지만
         혹시라도 입력받은 진행일이 유효하지 않은 경우? 1. 개설가능한 날짜 범위(개설당일+3일 ~ 14일?) 외, 2. 이미 개설된 날짜거나*/

//        // 1. 개설가능한 날짜 범위 외
//        // 개설가능한 날짜(firstAvailableDate ~ lastAvailableDate)
//        LocalDate firstAvailableDate = LocalDate.now().plusDays(3); // 오늘로부터 3일 이후
//        LocalDate lastAvailableDate = firstAvailableDate.plusDays(14); // 최초 개설가능일~14일
//        List<LocalDate> inValidDates = new ArrayList<>();
//        for (LocalDate date : publishedProgramCreateDto.getDates()) {
//            if (date.isBefore(firstAvailableDate) || date.isAfter(lastAvailableDate))
//                inValidDates.add(date);
//        }
//        // 예외처리 필요
//        if (!inValidDates.isEmpty()) {
//            System.out.println("프로그램 개설 실패: 유효하지 않은 날짜.");
//            inValidDates.forEach(System.out::println);
//            System.out.println("개설가능일: 3일 후부터 2주간");
//            return null;
//        }
//
//        // 2. 이미 개설된 날짜.
//        List<PublishedProgram> activePublishedPrograms = publishedProgramRepository.findAll(List.of(publishedProgramCreateDto.getRegMemberId()),
//                firstAvailableDate, lastAvailableDate,
//                Arrays.asList(STATUS_ON_GOING, STATUS_URGENT, STATUS_CONFIRMED, STATUS_WAIT_CONFIRM), null);
//
//        List<LocalDate> unavailableDates = new ArrayList<>(activePublishedPrograms.stream().map(PublishedProgram::getDate).toList());
//        System.out.println(unavailableDates);
//
//        unavailableDates.retainAll(publishedProgramCreateDto.getDates());
//
//        if (!unavailableDates.isEmpty()) {
//            System.out.println("프로그램 개설 실패: 해당 일자에 이미 개설된 프로그램이 존재합니다.");
//            unavailableDates.forEach(System.out::println);
//            return null;
//        }
        // </editor-fold>

        // ** PublishedProgram엔티티에 필드로 등록될 값들
        // 개설할 프로그램 entity
        Program program = programRepository.findById(publishedProgramCreateDto.getProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Program not found with ID: " + publishedProgramCreateDto.getProgramId()));
        // 프로그램의 상태를 PUblished로 변경
        program.setStatus("Published");

        // 개설된 프로그램의 status 초기값
        final Long INITIAL_STATUS_ID = STATUS_ON_GOING;
        Status initialStatus = statusRepository.findById(INITIAL_STATUS_ID)
                .orElseThrow(() -> new EntityNotFoundException("Status not found with ID: " + INITIAL_STATUS_ID));


        // 개설할 프로그램 리스트 만들어서 saveAll(개설된 프로그램 리스트)
        List<PublishedProgram> publishedPrograms = publishedProgramCreateDto.getDates().stream()
                .map(date -> PublishedProgram.builder()
                        .program(program)
                        .date(date)
                        .groupSizeCurrent(0) // 초기값 0
                        .status(initialStatus)
                        .build())
                .toList();

        // 개설한 프로그램의 상태를 Published로 변경
        programRepository.save(program);

        List<PublishedProgram> savedPublishedPrograms = publishedProgramRepository.saveAll(publishedPrograms);

        return PublishedProgramCreatedDto.builder()
                .programId(program.getId())
                .programTitle(program.getTitle())
                .regMemberId(publishedProgramCreateDto.getRegMemberId())
                .dates(publishedProgramCreateDto.getDates())
                .build();
    }

    @Override
    @Transactional
    public PublishedProgramUpdatedDto update(PublishedProgramUpdateDto publishedProgramUpdateDto) {
        // 1. publishedProgram id로 update할 객체 꺼내오기
        PublishedProgram publishedProgram = publishedProgramRepository.findById(publishedProgramUpdateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("publishedProgram not found with ID: " + publishedProgramUpdateDto.getId()));
        final long OLD_STATUS = publishedProgram.getStatus().getId();

        // 2. 입력받은 statusId로 publishedProgram에 세팅할 status꺼내오기
        Status newStatus = statusRepository.findById(publishedProgramUpdateDto.getStatusId())
                .orElseThrow(() -> new EntityNotFoundException("status not found with ID: " + publishedProgramUpdateDto.getStatusId()));

        // 3. setter로 값 변경
        // <editor-fold desc="유효성 검사코드">
        /*
            유효성검사
            1. 업데이트할 publishedProgram의 유효성
                - 엔티티가 db에 존재하는지? 이미 위에서 처리됨.
            2. publishedProgramUpdateDto의 유효성
                - publishedProgramUpdateDto로 받아온 groupSizeCurrent in program엔티티의 [0, groupSizeMax]?
                - 꺼내온 publishedProgram가 publishedProgramUpdateDto의 값으로 update될 수 있는 상태인지?
        */

        // Canceled or Finished는 update 불가능
        if (OLD_STATUS == STATUS_CANCELED || OLD_STATUS == STATUS_FINISHED)
            throw new RuntimeException("Cannot update publishedProgram with status: " + publishedProgram.getStatus().getName());

        // Confirmed -> Finished or Canceled 외 불가능
        if (OLD_STATUS == STATUS_CONFIRMED && !(newStatus.getId() == STATUS_CANCELED || newStatus.getId() == STATUS_FINISHED)) {
            String errorMessage = String.format("Cannot update publishedProgram with status '%s' to status '%s'.",
                    publishedProgram.getStatus().getName(), newStatus.getName());
            throw new RuntimeException(errorMessage);
        }

        // Confirmed 제외한 나머지 상태에서 Finished 불가능
        if (OLD_STATUS != STATUS_CONFIRMED && newStatus.getId() == STATUS_FINISHED) {
            String errorMessage = String.format("Cannot update publishedProgram with status '%s' to status '%s'.",
                    publishedProgram.getStatus().getName(), newStatus.getName());
            throw new RuntimeException(errorMessage);
        }

        // Urgent -> On Going 불가능
        if (OLD_STATUS == STATUS_URGENT && newStatus.getId() == STATUS_ON_GOING) {
            String errorMessage = String.format("Cannot update publishedProgram with status '%s' to status '%s'.",
                    publishedProgram.getStatus().getName(), newStatus.getName());
            throw new RuntimeException(errorMessage);
        }

        // groupSizeCurrent는 program의 [0, groupSizeMax]에 포함되어야함
        if (publishedProgram.getProgram().getGroupSizeMax() < publishedProgramUpdateDto.getGroupSizeCurrent()
                || publishedProgramUpdateDto.getGroupSizeCurrent() < 0) {
            String errorMessage = String.format("Invalid group size: The group size (%d) must be between 0 and %d.",
                    publishedProgramUpdateDto.getGroupSizeCurrent(),
                    publishedProgram.getProgram().getGroupSizeMax());
            throw new RuntimeException(errorMessage);
        }
        //</editor-fold>
        publishedProgram.setGroupSizeCurrent(publishedProgramUpdateDto.getGroupSizeCurrent());
        publishedProgram.setStatus(newStatus);

        // 4. repository.save로 persist
        publishedProgramRepository.save(publishedProgram);

        // 5. 저장된 publishedProgram을 다시 db에서 꺼내서 dto에 담아서 리턴
        PublishedProgram savedPublishedProgram = publishedProgramRepository.findById(publishedProgramUpdateDto.getId()).get();

        // modelMapper에서 쓸 Converter. List<Reservation> reservations를 List<Long> memberIds로
        // 6. 커스텀 modelMapper 설정
        Converter<List<Reservation>, List<Long>> reservationToMemberIdsConverter = ctx -> ctx.getSource().stream()
                .map(reservation -> reservation.getMember().getId())
                .toList();

        // modelMapper. PublishedProgram -> PublishedProgramUpdatedDto
        modelMapper.typeMap(PublishedProgram.class, PublishedProgramUpdatedDto.class)
                .addMappings(mapper -> mapper
                        .using(reservationToMemberIdsConverter)
                        .map(PublishedProgram::getReservations, PublishedProgramUpdatedDto::setBookingMemberIds));

        return modelMapper.map(savedPublishedProgram, PublishedProgramUpdatedDto.class);
    }
}