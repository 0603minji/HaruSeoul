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
    private ReservationRepository reservationRepository;
    private MemberRepository memberRepository;
    private StatusRepository statusRepository;

    public DefaultReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, StatusRepository statusRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.statusRepository = statusRepository;
    }


    //매퍼를 모델매퍼로 바꾸기
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
