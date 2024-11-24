package com.m2j2.haruseoul.guest.reservation.service;

import com.m2j2.haruseoul.entity.*;
import com.m2j2.haruseoul.guest.reservation.dto.*;
import com.m2j2.haruseoul.notification.dto.NotificationSendDto;
import com.m2j2.haruseoul.notification.service.NotificationService;
import com.m2j2.haruseoul.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class DefaultReservationService implements ReservationService {
    private final ProgramRepository programRepository;
    private final ImageRepository imageRepository;
    private ReservationRepository reservationRepository;
    private MemberRepository memberRepository;
    private StatusRepository statusRepository;
    private ReviewRepository reviewRepository;
    private RouteRepository routeRepository;
    private PublishedProgramRepository publishedProgramRepository;
    private final NotificationService notificationService;
    ModelMapper modelMapper;

    public DefaultReservationService(ReservationRepository reservationRepository,
                                     MemberRepository memberRepository,
                                     StatusRepository statusRepository,
                                     ReviewRepository reviewRepository,
                                     RouteRepository routeRepository,
                                     PublishedProgramRepository publishedProgramRepository,
                                     ModelMapper modelMapper, ProgramRepository programRepository, ImageRepository imageRepository, NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.statusRepository = statusRepository;
        this.reviewRepository = reviewRepository;
        this.routeRepository = routeRepository;
        this.publishedProgramRepository = publishedProgramRepository;
        this.modelMapper = modelMapper;
        this.programRepository = programRepository;
        this.imageRepository = imageRepository;
        this.notificationService = notificationService;
    }

    @Override
    public ReservationResponseDto getList(List<Long> sIds, List<Long> mIds, int pageNum) {
        // 기본적으로 정렬할 필드
        Sort sort;

        // sIds 목록에 따른 정렬 기준 설정
        if (!sIds.isEmpty()) {
            // 모든 상태가 [1, 2, 3, 4]에 포함되면 regDate 기준 내림차순
            if (sIds.containsAll(Arrays.asList(1L, 2L, 3L, 4L))) {
                sort = Sort.by(Sort.Order.desc("regDate"));
            } else {
                // 특정 상태에 따른 정렬
                Long statusId = sIds.get(0); // 첫 번째 값으로 상태 ID 판단
                switch (statusId.intValue()) {
                    case 1: // 결제완료
                    case 2: // 예약확정
                        sort = Sort.by(Sort.Order.asc("publishedProgram.date")); // 날짜 오름차순
                        break;
                    case 3: // 취소됨
                        sort = Sort.by(Sort.Order.desc("deleteDate")); // 삭제 날짜 내림차순
                        break;
                    case 4: // 이용완료
                        sort = Sort.by(Sort.Order.desc("publishedProgram.date")); // 날짜 내림차순
                        break;
                    default:
                        sort = Sort.by(Sort.Order.desc("regDate")); // 기본값
                        break;
                }
            }
        } else {
            // sIds가 비어있다면 기본값으로 regDate 내림차순
            sort = Sort.by(Sort.Order.desc("regDate"));
        }

        Pageable pageable = PageRequest.of(pageNum - 1, 6, sort);

        // 요청 페이지 번호 확인
        System.out.println("Requesting page number: " + (pageNum - 1));

        // Status ID가 없는 경우 해당 회원의 전체 예약 조회, Status ID가 있으면
        Page<Reservation> reservations = reservationRepository.findAll(sIds, mIds, pageable);

        modelMapper.typeMap(Reservation.class, ReservationListDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getPublishedProgram().getDate(), ReservationListDto::setDate);
                    mapper.map(src -> src.getPublishedProgram().getProgram().getId(), ReservationListDto::setProgramId);
                    mapper.map(src -> src.getPublishedProgram().getProgram().getTitle(), ReservationListDto::setProgramTitle);
                    mapper.map(src -> src.getPublishedProgram().getProgram().getMember().getId(), ReservationListDto::setHostId);
                    mapper.map(src -> src.getPublishedProgram().getStatus().getName(), ReservationListDto::setStatusName);
                    mapper.map(src -> src.getPublishedProgram().getId(), ReservationListDto::setPublishedProgramId);
                });

        // 결과 확인
        System.out.println("Total reservations fetched: " + reservations.getTotalElements());

        List<ReservationListDto> reservationListDto = reservations.getContent()
                .stream()
                .map(reservation -> {
                    ReservationListDto dto = modelMapper.map(reservation, ReservationListDto.class);

                    // publishedProgramId로 programId 조회
                    Long publishedProgramId = publishedProgramRepository.findProgramIdByPublishedProgramId(reservation.getPublishedProgram().getId());

                    // programId로 이미지 src 조회
                    String image = String.valueOf(imageRepository.findFirstSrcByProgramId(publishedProgramId));
                    dto.setSrc(image);

                    return dto;
                })
                .toList();


        long totalRowCount = reservations.getTotalElements();
        long totalPageCount = reservations.getTotalPages();

        return ReservationResponseDto.builder()
                .reservations(reservationListDto)
                .totalRowCount(totalRowCount)
                .totalPageCount(totalPageCount)
                .build();
    }

    @Override
    @Transactional
    public ReservationDetailResponseDto getDetail(Long rid) {
        // 예약 테이블에서 reservationId 가 일치하는 것을 담는다.
        Reservation reservation = reservationRepository.findById(rid)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        // ratingAverage 를 따로 담아오기
        modelMapper.typeMap(reservation.getClass(), ReservationListDto.class)
                .addMappings(mapper -> {
                    mapper.map(Reservation::getId, ReservationListDto::setId);
                    mapper.map(src -> src.getPublishedProgram().getProgram().getMember().getId(), ReservationListDto::setHostId);
                    mapper.map(src -> src.getPublishedProgram().getStatus().getName(), ReservationListDto::setStatusName);
                    mapper.map(src -> src.getPublishedProgram().getProgram().getTitle(), ReservationListDto::setProgramTitle);
                    mapper.map(src -> src.getPublishedProgram().getProgram().getId(), ReservationListDto::setProgramId);
                    mapper.map(src -> src.getPublishedProgram().getDate(), ReservationListDto::setDate);
                    mapper.map(Reservation::getNumberOfGuest, ReservationListDto::setNumberOfGuest);
                    mapper.map(src -> src.getPublishedProgram().getId(), ReservationListDto::setPublishedProgramId);
                });


        // ReservationListDto reservationCard
        ReservationListDto reservationListDto = modelMapper.map(reservation, ReservationListDto.class);

        Long programId = publishedProgramRepository.findProgramIdByPublishedProgramId(reservation.getPublishedProgram().getId());

        // programId로 이미지 src 조회
        String image = String.valueOf(imageRepository.findFirstSrcByProgramId(programId));
        reservationListDto.setSrc(image);

        // ReservationDetailProgramDto program;
        // 예약한 공개 프로그램
        Program reservationProgram = programRepository.findById(programId).orElse(null);

        // 프로그램 ID 에 해당하는 Route 가져오기
        Route meetingSpot = routeRepository.meetingSpotTitleByProgramId(reservationProgram.getId());
        // meetingSpotTitle: meetingSpot 의 이름 담기
        String meetingSpotTitle = meetingSpot.getTitle();
        System.out.println(meetingSpotTitle);
        // meetingSpotAddress: meetingSpot 의 주소 담기
        String meetingSpotAddress = meetingSpot.getAddress();
        System.out.println(meetingSpotAddress);
        ReservationDetailProgramDto reservationDetailProgramDto = ReservationDetailProgramDto.builder()
                .programId(reservationProgram.getId())
                .programStartTime(String.valueOf(reservationProgram.getStartTime()))
                .meetingSpotTitle(meetingSpotTitle)
                .meetingSpotAddress(meetingSpotAddress)
                .programInclusion(reservationProgram.getInclusion() != null
                        ? reservationProgram.getInclusion().trim()
                        : "")
                .programExclusion(reservationProgram.getExclusion() != null
                        ? reservationProgram.getExclusion().trim()
                        : "")
                .programPackingList(reservationProgram.getPackingList() != null
                        ? reservationProgram.getPackingList().trim()
                        : "")
                .programCaution(reservationProgram.getCaution() != null
                        ? reservationProgram.getCaution().trim()
                        : "")
                .reservationRequirement(reservationProgram.getRequirement() != null
                        ? reservationProgram.getRequirement().trim()
                        : "")
                .publishedProgramId(reservation.getPublishedProgram().getId())
                .publishedProgramStatus(reservation.getPublishedProgram().getStatus().getId())
                .build();


        // ReservationDetailGuestDto guest;
        ReservationDetailGuestDto reservationDetailGuestDto = ReservationDetailGuestDto.builder()
                .memberName(reservation.getMember().getName())
                .memberEmail(String.valueOf(reservation.getMember().getEmail()))
                .build();

        // ReservationDetailHostDto Host;
        // ratingCount: 특정 memberId 에 해당하는 review 의 개수 담기
        Long ratingCount = reviewRepository.countByMemberId(reservation.getPublishedProgram().getProgram().getMember().getId());
        Long totalRating = reviewRepository.sumRatingByMemberId(reservation.getPublishedProgram().getProgram().getMember().getId()); // 모든 rating 값의 합
        Double averageRating = ratingCount != 0 ? totalRating / ratingCount : 0.0;
        ReservationDetailHostDto reservationDetailHostDto = ReservationDetailHostDto.builder()
                .memberImg(reservation.getPublishedProgram().getProgram().getMember().getProfileImgSrc())
                .memberName(reservation.getPublishedProgram().getProgram().getMember().getName())
                .programRating(String.valueOf(averageRating))
                .ratingCount(ratingCount)
                .build();

        // ReservationDetailRequirementDto guest
        ReservationDetailRequirementDto reservationDetailRequirementDto =
                ReservationDetailRequirementDto.builder()
                        .guestRequirement(reservation.getRequirement())
                        .hostRequirement(reservation.getPublishedProgram().getProgram().getRequirement())
                        .build();

        // ReservationDetailRequirementDto host

        return ReservationDetailResponseDto.builder()
                .reservationId(reservation.getId())
                .reservationCard(reservationListDto)
                .program(reservationDetailProgramDto)
                .guest(reservationDetailGuestDto)
                .host(reservationDetailHostDto)
                .requirement(reservationDetailRequirementDto)
                .build();
    }

    @Override
    @Transactional
    public ReservationCreatedDto create(ReservationCreateDto reservationCreateDto) {

        // 예약할때 받은 공개 프로그램 ID로 공개 프로그램 가져오기
        PublishedProgram publishedProgram = publishedProgramRepository.findById(reservationCreateDto.getPublishedProgramId())
                .orElseThrow(() -> new IllegalArgumentException("PublishedProgram not found")
                );

        // 예약하는 멤버 ID로 멤버 가져오기
        Member member = memberRepository.findById(reservationCreateDto.getGuestId()).orElseThrow(() ->
                new IllegalArgumentException("MemberId not found with id")
        );

        Reservation reservation = Reservation.builder()
                .publishedProgram(publishedProgram)
                .member(member)
                .numberOfGuest(reservationCreateDto.getNumberOfGuest())
                .reservationStatus(1)
                .requirement(reservationCreateDto.getReservationRequirement())
                .build();

        reservationRepository.save(reservation);
        System.out.println(reservation.getId());

        // reservation 의 group_size 들로 해당 publishedProgram 의 group_size_current 합산
        int groupSizeCurrent = publishedProgram.getGroupSizeCurrent(); // 공개된 프로그램의 현재 진행 인원 수
        int reservationGroupSize = reservationCreateDto.getNumberOfGuest(); // 예약 인원 수

        Program reservationProgram = programRepository.findById(publishedProgram.getProgram().getId()).orElse(null);
        assert reservationProgram != null;
        int programGroupMaxSize = reservationProgram.getGroupSizeMax(); // 프로그램의 지정된 최대 인원 수

        // 혹시나 0명 예약할 수도 있으니, 합계가 0 보다 크거나 프로그램 지정 최대 인원 수보다 작은 경우에만 publishedProgram 에 값이 저장되고 , 예약이 진행된다.
        if (0 < groupSizeCurrent + reservationGroupSize && groupSizeCurrent + reservationGroupSize < programGroupMaxSize + 1) {
            publishedProgramRepository.save(publishedProgram);  // 갱신된 값 저장
            int updatedGroupSize = groupSizeCurrent + reservationGroupSize;
            publishedProgram.setGroupSizeCurrent(updatedGroupSize);

            Status WaitConfirm = statusRepository.findById(5L).orElseThrow(() ->
                    new IllegalArgumentException("Status not found"));
            if (updatedGroupSize == programGroupMaxSize) publishedProgram.setStatus(WaitConfirm);
        } else {
            throw new IllegalStateException("예약 불가: 총 그룹 인원이 " + programGroupMaxSize + " 명을 초과할 수 없습니다.");
        }

        //============알림부분=====================
        Long senderId = reservation.getMember().getId();
        Long programId = reservation.getPublishedProgram().getId();
        NotificationSendDto notificationSendDto = NotificationSendDto.builder()
                .senderId(senderId)
                .programId(programId)
                .type("RESERVE")
                .build();
        notificationService.sendFromGuest(notificationSendDto);


        return null;
    }

    @Override
    @Transactional
    public void delete(Long reservationId) {
        // 예약 ID로 해당 예약을 찾아옵니다.
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        // 예약 상태를 취소됨으로 바꾸기
        reservation.setReservationStatus(3);
        // 예약에 삭제 날짜 추가하기
        Date deleteDate = new Date(); // 현재 시간
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(deleteDate);
        calendar.add(Calendar.HOUR, 9); // 9시간 추가
        deleteDate = calendar.getTime();
        reservation.setDeleteDate(deleteDate.toInstant());
        reservationRepository.save(reservation);


        // 로그를 남겨서 예약 삭제에 대한 기록을 남길 수 있습니다.
        System.out.println("Reservation with ID " + reservationId + " has been soft deleted at " + Instant.now());


        //==========알림부분=====================
        Long senderId = reservation.getMember().getId();
        Long programId = reservation.getPublishedProgram().getId();

        NotificationSendDto notificationSendDto = NotificationSendDto.builder()
                .senderId(senderId)
                .programId(programId)
                .type("CANCEL")
                .build();
        notificationService.sendFromGuest(notificationSendDto);


    }
}
