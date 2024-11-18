package com.m2j2.haruseoul.guest.reservation.service;

import com.m2j2.haruseoul.entity.*;
import com.m2j2.haruseoul.guest.reservation.dto.*;
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
    ModelMapper modelMapper;

    public DefaultReservationService(ReservationRepository reservationRepository,
                                     MemberRepository memberRepository,
                                     StatusRepository statusRepository,
                                     ReviewRepository reviewRepository,
                                     RouteRepository routeRepository,
                                     PublishedProgramRepository publishedProgramRepository,
                                     ModelMapper modelMapper, ProgramRepository programRepository, ImageRepository imageRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.statusRepository = statusRepository;
        this.reviewRepository = reviewRepository;
        this.routeRepository = routeRepository;
        this.publishedProgramRepository = publishedProgramRepository;
        this.modelMapper = modelMapper;
        this.programRepository = programRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public ReservationResponseDto getList(List<Long> sIds, List<Long> mIds, Boolean isDeleted, int pageNum) {

        Sort sort = Sort.by("publishedProgram.date").ascending();
        Pageable pageable = PageRequest.of(pageNum-1, 6, sort);

        // 요청 페이지 번호 확인
        System.out.println("Requesting page number: " + (pageNum - 1));

        // Status ID가 없는 경우 해당 회원의 전체 예약 조회, Status ID가 있으면
        Page<Reservation> reservations = reservationRepository.findAll(sIds, mIds, isDeleted, pageable);

        modelMapper.typeMap(Reservation.class, ReservationListDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getPublishedProgram().getDate(), ReservationListDto::setDate);
                    mapper.map(src -> src.getPublishedProgram().getProgram().getTitle(), ReservationListDto::setProgramTitle);
                    mapper.map(src -> src.getPublishedProgram().getProgram().getMember().getId(), ReservationListDto::setHostId);
                    mapper.map(src -> src.getPublishedProgram().getStatus().getName(), ReservationListDto::setStatusName);
                });

        // 결과 확인
        System.out.println("Total reservations fetched: " + reservations.getTotalElements());

        List<ReservationListDto> reservationListDto = reservations.getContent()
                .stream()
                .map(reservation -> {
                    ReservationListDto dto = modelMapper.map(reservation, ReservationListDto.class);

                    // publishedProgramId로 programId 조회
                    Long programId = publishedProgramRepository.findProgramIdByPublishedProgramId(reservation.getPublishedProgram().getId());

                    // programId로 이미지 src 조회
                    String image = String.valueOf(imageRepository.findFirstSrcByProgramId(programId));
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
                    mapper.map(src -> src.getPublishedProgram().getDate(), ReservationListDto::setDate);
                    mapper.map(Reservation::getNumberOfGuest, ReservationListDto::setNumberOfGuest);
                });


        // ReservationListDto reservationCard
        ReservationListDto reservationListDto = modelMapper.map(reservation, ReservationListDto.class);

        Long programId = publishedProgramRepository.findProgramIdByPublishedProgramId(reservation.getPublishedProgram().getId());
        String image = String.valueOf(imageRepository.findFirstSrcByProgramId(programId));
        reservationListDto.setSrc(image);

        // ReservationDetailProgramDto program;
        // 예약한 공개 프로그램
        Program reservationProgram = programRepository.findById(reservation.getPublishedProgram().getProgram().getId()).orElse(null);

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
                .programInclusion(String.valueOf(reservationProgram.getInclusion()))
                .programExclusion(String.valueOf(reservationProgram.getExclusion()))
                .programPackingList(String.valueOf(reservationProgram.getPackingList()))
                .programCaution(String.valueOf(reservationProgram.getCaution()))
                .reservationRequirement(String.valueOf(reservationProgram.getRequirement()))
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
        }
        else {
            throw new IllegalStateException("예약 불가: 총 그룹 인원이 " + programGroupMaxSize + " 명을 초과할 수 없습니다.");
        }

        return null;
    }

    @Override
    @Transactional
    public void delete(Long reservationId) {
        // 예약 ID로 해당 예약을 찾아옵니다.
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        // 삭제 날짜를 현재 시간으로 설정합니다.
        reservation.setDeleteDate(Instant.now());  // 삭제 일자를 현재 시간으로 설정 (LocalDateTime으로 변경 가능)

        // 예약을 업데이트합니다.
        reservationRepository.save(reservation);


        // 로그를 남겨서 예약 삭제에 대한 기록을 남길 수 있습니다.
        System.out.println("Reservation with ID " + reservationId + " has been soft deleted at " + Instant.now());
    }
}
