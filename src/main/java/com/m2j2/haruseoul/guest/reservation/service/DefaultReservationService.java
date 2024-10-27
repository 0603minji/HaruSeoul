package com.m2j2.haruseoul.guest.reservation.service;

import com.m2j2.haruseoul.entity.*;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationCreateDto;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationListDto;
import com.m2j2.haruseoul.guest.reservation.dto.ReservationResponseDto;
import com.m2j2.haruseoul.guest.reservation.mapper.ReservationMapper;
import com.m2j2.haruseoul.repository.*;
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
    private final PublishedProgramRepository publishedProgramRepository;
    private ReservationRepository reservationRepository;
    private MemberRepository memberRepository;
    private StatusRepository statusRepository;

    public DefaultReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, StatusRepository statusRepository, PublishedProgramRepository publishedProgramRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.statusRepository = statusRepository;
        this.publishedProgramRepository = publishedProgramRepository;
    }

    @Override
    public ReservationResponseDto getReservationByStatus(Long sId) {
        Sort sort = Sort.by("regDate").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);

        // Status ID가 없는 경우 전체 조회 수행
        Page<Reservation> reservationPage;
        if (sId == null) {
            reservationPage = reservationRepository.findAll(pageable);
        } else {
            reservationPage = reservationRepository.findReservationsByStatusId(sId, pageable);
        }


        List<ReservationListDto> reservationListDtos = reservationPage.getContent()
                .stream()
                .map(ReservationMapper::mapToDto)
                .toList();

        long totalRowCount = reservationPage.getTotalElements();
        long totalPageCount = reservationPage.getTotalPages();

        return ReservationResponseDto.builder()
                .reservations(reservationListDtos)
                .totalRowCount(totalRowCount)
                .totalPageCount(totalPageCount)
                .build();
    }

    @Override
    @Transactional
    public Reservation create(ReservationCreateDto reservationCreateDto) {

        Optional<PublishedProgram> regPublishedProgram = publishedProgramRepository.findById(reservationCreateDto.getPublishedProgramId());
        Optional<Member> regMember = memberRepository.findById(reservationCreateDto.getRegMemberId());

        Reservation reservation = Reservation.builder()
                .publishedProgram(regPublishedProgram.get())
                .member(regMember.get())
                .regDate(reservationCreateDto.getProgramDate())
                .groupSize(reservationCreateDto.getGroupSize())
                .build();


        return reservationRepository.save(reservation);
    }

    @Override
    public void delete(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
