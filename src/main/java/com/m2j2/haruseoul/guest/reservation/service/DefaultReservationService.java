package com.m2j2.haruseoul.guest.reservation.service;

import com.m2j2.haruseoul.entity.*;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationCreateDto;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationDetailDto;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationListDto;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationResponseDto;
import com.m2j2.haruseoul.guest.reservation.mapper.ReservationMapper;
import com.m2j2.haruseoul.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultReservationService implements ReservationService {
    private ReservationRepository reservationRepository;
    private MemberRepository memberRepository;
    private StatusRepository statusRepository;
    private ReviewRepository reviewRepository;
    private RouteRepository routeRepository;
    ModelMapper modelMapper;

    public DefaultReservationService(ReservationRepository reservationRepository,
                                     MemberRepository memberRepository,
                                     StatusRepository statusRepository,
                                     ReviewRepository reviewRepository,
                                     RouteRepository routeRepository,
                                     ModelMapper modelMapper) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.statusRepository = statusRepository;
        this.reviewRepository = reviewRepository;
        this.routeRepository = routeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReservationResponseDto getList(List<Long> sIds, List<Long> mIds) {
        Sort sort = Sort.by("regDate").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);

        // Status ID가 없는 경우 해당 회원의 전체 예약 조회, Status ID가 있으면
        Page<Reservation> reservations = reservationRepository.findAll(sIds, mIds, pageable);

        List<ReservationListDto> reservationListDtos = reservations.getContent()
                .stream()
                .map(ReservationMapper::mapToDto)
                .toList();

        long totalRowCount = reservations.getTotalElements();
        long totalPageCount = reservations.getTotalPages();

        return ReservationResponseDto.builder()
                .reservations(reservationListDtos)
                .totalRowCount(totalRowCount)
                .totalPageCount(totalPageCount)
                .build();
    }

    @Override
    public ReservationDetailDto getDetail(Long rid) {

        // 예약 테이블에서 reservationId 가 일치하는 것을 담는다.
        Reservation reservation = reservationRepository.findById(rid).orElse(null);

        // 예약한 공개 프로그램의 프로그램 Id
        Long pId = reservation.getPublishedProgram().getProgram().getId();
        // ratingCount: review 의 개수 담기
        Long reviewsCount = reviewRepository.countByProgramId(pId);
        // routeTitle: route 의 이름 담기
        String routeTitle = routeRepository.titleByProgramId(pId);
        // routeAddress: route 의 주소 담기
        String routeAddress = routeRepository.addressByProgramId(pId);

        return ReservationDetailDto.builder()
                .reservationId(reservation.getId())
                .guestName(reservation.getMember().getName())
                .guestEmail(reservation.getMember().getEmail())
//                .guestPhoneNumber(reservation.getMember().getNumber())
                .hostName(reservation.getPublishedProgram().getProgram().getMember().getName())
                .programRating(String.valueOf(reservation.getPublishedProgram().getProgram().getRating())) // 프로그램 테이블의 rating 이 평균 평점이라고 생각하고 만듬
                .ratingCount(String.valueOf(reviewsCount))
                .programStartTime(String.valueOf(reservation.getPublishedProgram().getProgram().getStartTime()))
                .routeTitle(routeTitle)
                .routeAddress(routeAddress)
                .programInclusion(String.valueOf(reservation.getPublishedProgram().getProgram().getInclusion()))
                .programExclusion(String.valueOf(reservation.getPublishedProgram().getProgram().getExclusion()))
                .programPackingList(String.valueOf(reservation.getPublishedProgram().getProgram().getPackingList()))
                .programCaution(String.valueOf(reservation.getPublishedProgram().getProgram().getCaution()))
                .programRequirement(String.valueOf(reservation.getPublishedProgram().getProgram().getRequirement()))
                .build();

//        return (ReservationDetailDto) modelMapper.typeMap(Reservation.class, ReservationDetailDto.class)
//                .addMappings(mapper -> {
//                    mapper.map(Reservation::getId, ReservationDetailDto::setReservationId);
//                    mapper.map(src -> src.getMember().getName(), ReservationDetailDto::setGuestName);
//                    mapper.map(src -> src.getMember().getEmail(), ReservationDetailDto::setGuestEmail);
//
//                    // 호스트 정보와 프로그램 관련 매핑
//                    mapper.map(src -> src.getPublishedProgram().getProgram().getMember().getName(),
//                            ReservationDetailDto::setHostName);
//                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getRating()),
//                            ReservationDetailDto::setProgramRating);
//                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getStartTime()),
//                            ReservationDetailDto::setProgramStartTime);
//                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getInclusion()),
//                            ReservationDetailDto::setProgramInclusion);
//                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getExclusion()),
//                            ReservationDetailDto::setProgramExclusion);
//                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getPackingList()),
//                            ReservationDetailDto::setProgramPackingList);
//                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getCaution()),
//                            ReservationDetailDto::setProgramCaution);
//                    mapper.map(src -> String.valueOf(src.getPublishedProgram().getProgram().getRequirement()),
//                            ReservationDetailDto::setProgramRequirement);
//                });
//                ratingCount, routeTitle, routeAddress 를 어떻게 Model Mapper 에 담는가.. 못하겠다
    }

    @Override
    @Transactional
    public Reservation create(ReservationCreateDto reservationCreateDto) {

//        Optional<PublishedProgram> regPublishedProgram = publishedProgramRepository.findById(reservationCreateDto.getPublishedProgramId());
//        Optional<Member> regMember = memberRepository.findById(reservationCreateDto.getRegMemberId());
//
//        Reservation reservation = Reservation.builder()
//                .publishedProgram(regPublishedProgram.get())
//                .member(regMember.get())
//                .regDate(reservationCreateDto.getProgramDate())
//                .groupSize(reservationCreateDto.getGroupSize())
//                .build();
//
//
//        return reservationRepository.save(reservation);
        return null;
    }

    @Override
    public void delete(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
