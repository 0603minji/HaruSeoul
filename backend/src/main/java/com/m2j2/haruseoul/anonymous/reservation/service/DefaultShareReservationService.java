package com.m2j2.haruseoul.anonymous.reservation.service;

import com.m2j2.haruseoul.anonymous.reservation.dto.ShareReservationDetailResponseDto;
import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.entity.Route;
import com.m2j2.haruseoul.guest.reservation.dto.*;
import com.m2j2.haruseoul.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultShareReservationService implements ShareReservationService{
    private final ProgramRepository programRepository;
    private final ImageRepository imageRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final RouteRepository routeRepository;
    private final PublishedProgramRepository publishedProgramRepository;
    ModelMapper modelMapper;

    public DefaultShareReservationService(ReservationRepository reservationRepository,
                                     ReviewRepository reviewRepository,
                                     RouteRepository routeRepository,
                                     PublishedProgramRepository publishedProgramRepository,
                                     ModelMapper modelMapper,
                                     ProgramRepository programRepository,
                                     ImageRepository imageRepository) {
        this.reservationRepository = reservationRepository;
        this.reviewRepository = reviewRepository;
        this.routeRepository = routeRepository;
        this.publishedProgramRepository = publishedProgramRepository;
        this.modelMapper = modelMapper;
        this.programRepository = programRepository;
        this.imageRepository = imageRepository;
    }


    @Override
    @Transactional
    public ShareReservationDetailResponseDto getDetail(Long rid) {
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
                    mapper.map(src -> src.getPublishedProgram().getId(), ReservationListDto::setPublishedProgramId);
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
        return ShareReservationDetailResponseDto.builder()
                .reservationId(reservation.getId())
                .reservationCard(reservationListDto)
                .program(reservationDetailProgramDto)
                .guest(reservationDetailGuestDto)
                .host(reservationDetailHostDto)
                .requirement(reservationDetailRequirementDto)
                .build();
    }
}
