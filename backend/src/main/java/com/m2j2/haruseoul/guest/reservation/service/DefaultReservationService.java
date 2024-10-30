package com.m2j2.haruseoul.guest.reservation.service;

import com.m2j2.haruseoul.entity.*;
import com.m2j2.haruseoul.guest.reservation.dto.*;
import com.m2j2.haruseoul.guest.reservation.mapper.ReservationMapper;
import com.m2j2.haruseoul.repository.*;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultReservationService implements ReservationService {
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
                                     ModelMapper modelMapper) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.statusRepository = statusRepository;
        this.reviewRepository = reviewRepository;
        this.routeRepository = routeRepository;
        this.publishedProgramRepository = publishedProgramRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReservationResponseDto getList(List<Long> sIds, List<Long> mIds) {
        Sort sort = Sort.by("regDate").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);

        // Status ID가 없는 경우 해당 회원의 전체 예약 조회, Status ID가 있으면
        Page<Reservation> reservations = reservationRepository.findAll(sIds, mIds, pageable);

        List<ReservationListDto> reservationListDto = reservations.getContent()
                .stream()
                .map(ReservationMapper::mapToDto)
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

        // 명시적으로 초기화 (지연 로딩 해결)
        Hibernate.initialize(reservation.getMember());

        // 예약한 공개 프로그램의 프로그램 Id
        Long pId = reservation.getPublishedProgram().getProgram().getId();
        // ratingCount: review 의 개수 담기 - memberId 로 가져오기
        Long reviewsCount = reviewRepository.countByProgramId(pId);
        // ratingAverage 를 따로 담아오기

        // routeTitle: route 의 이름 담기
        String routeTitle = routeRepository.titleByProgramId(pId);
        // routeAddress: route 의 주소 담기
        String routeAddress = routeRepository.addressByProgramId(pId);

        // ReservationListDto 만들기
        ReservationListDto reservationListDto = modelMapper.map(reservation, ReservationListDto.class);

        modelMapper.typeMap(Reservation.class, ReservationListDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getPublishedProgram().getProgram().getMember().getId(), ReservationListDto::setHostId);
                    mapper.map(src -> src.getPublishedProgram().getStatus().getName(), ReservationListDto::setStatusName);
                    mapper.map(src -> src.getPublishedProgram().getProgram().getTitle(), ReservationListDto::setProgramTitle);
                    mapper.map(src -> src.getPublishedProgram().getDate(), ReservationListDto::setDate);
                    mapper.map(Reservation::getGroupSize, ReservationListDto::setGroupSize);
                });


        // ReservationDetailDto 만들기
        ReservationDetailDto reservationDetailDto = modelMapper.map(reservation, ReservationDetailDto.class);

        modelMapper.typeMap(Reservation.class, ReservationDetailDto.class)
                .addMappings(mapper -> {
                    mapper.map(Reservation::getId, ReservationDetailDto::setReservationId);

                    // 호스트 정보와 프로그램 관련 매핑
                    mapper.map(src -> src.getPublishedProgram().getProgram().getMember().getName(),
                            ReservationDetailDto::setHostName);
                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getRating()),
                            ReservationDetailDto::setProgramRating);
                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getStartTime()),
                            ReservationDetailDto::setProgramStartTime);
                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getInclusion()),
                            ReservationDetailDto::setProgramInclusion);
                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getExclusion()),
                            ReservationDetailDto::setProgramExclusion);
                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getPackingList()),
                            ReservationDetailDto::setProgramPackingList);
                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getCaution()),
                            ReservationDetailDto::setProgramCaution);
                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getRequirement()),
                            ReservationDetailDto::setReservationRequirement);
                });
        reservationDetailDto.setReviewsCount(reviewsCount);
        reservationDetailDto.setRouteTitle(routeTitle);
        reservationDetailDto.setRouteAddress(routeAddress);


        return ReservationDetailResponseDto.builder()
                .reservation(reservationListDto)
                .reservationDetail(reservationDetailDto)
                .build();
    }

    @Override
    @Transactional
    public Reservation create(ReservationCreateDto reservationCreateDto) {

        Long reservationPublishedProgramId = reservationCreateDto.getPublishedProgramId();
        PublishedProgram reservationPublishedProgram = publishedProgramRepository.findById(reservationPublishedProgramId).orElseThrow(() ->
                new IllegalArgumentException("PublishedProgram not found")
        );

        Long memberId = reservationCreateDto.getRegMemberId();
        Member reservationMember = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("MemberId not found with id: " + memberId)
        );

        Reservation reservation = Reservation.builder()
                .publishedProgram(reservationPublishedProgram)
                .member(reservationMember)
                .groupSize(reservationCreateDto.getReservationGroupSize())
                .requirement(reservationCreateDto.getReservationRequirement())
                .build();


        return reservationRepository.save(reservation);
    }

    @Override
    public void delete(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
