package com.m2j2.haruseoul.guest.publishedProgram.service;

import com.m2j2.haruseoul.entity.PublishedProgram;
import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.entity.Status;
import com.m2j2.haruseoul.guest.publishedProgram.dto.GuestPublishedProgramUpdateDto;
import com.m2j2.haruseoul.guest.publishedProgram.dto.GuestPublishedProgramUpdatedDto;
import com.m2j2.haruseoul.repository.PublishedProgramRepository;
import com.m2j2.haruseoul.repository.ReservationRepository;
import com.m2j2.haruseoul.repository.StatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

@Service
public class DefaultGuestPublishedProgramService implements GuestPublishedProgramService {

    private final PublishedProgramRepository publishedProgramRepository;
    private final ReservationRepository reservationRepository;
    StatusRepository statusRepository;

    public DefaultGuestPublishedProgramService(PublishedProgramRepository publishedProgramRepository,
                                               StatusRepository statusRepository,
                                               ReservationRepository reservationRepository) {
        this.publishedProgramRepository = publishedProgramRepository;
        this.statusRepository = statusRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional
    public GuestPublishedProgramUpdatedDto update(GuestPublishedProgramUpdateDto guestPublishedProgramUpdateDto) {
        Reservation reservation = reservationRepository.findById(guestPublishedProgramUpdateDto.getReservationId())
                .orElseThrow(() -> new EntityNotFoundException("reservation not found with ID: " + guestPublishedProgramUpdateDto.getReservationId()));

        PublishedProgram publishedProgram = publishedProgramRepository.findById(guestPublishedProgramUpdateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("publishedProgram not found with ID: " + guestPublishedProgramUpdateDto.getId()));

        // 현재 공개프로그램 총 인원수
        int currentGroupSize = publishedProgram.getGroupSizeCurrent();
        
        // 취소하는 예약 인원 수
        int numberOfGuest = reservation.getNumberOfGuest();

        // 예약이 취소되고난 이후 인원
        int updatedGroupSize = currentGroupSize - numberOfGuest;
        publishedProgram.setGroupSizeCurrent(updatedGroupSize);

        // 공개프로그램의 상태
        Long statusId = publishedProgram.getStatus().getId();

        LocalDate publishedProgramDate = publishedProgram.getDate();
        LocalDate today = LocalDate.now();

        // 프로그램 최소인원
        int programGroupSizeMin = publishedProgram.getProgram().getGroupSizeMin();

        // 공개프로그램이 예약확정 일때는, 현재 인원이 0명이 되면 상태가 취소됨으로 자동 변경되어야 함
        if (statusId == 6L && updatedGroupSize == 0) {
            publishedProgram.setStatus(statusRepository.findById(4L).get());
        }
        if(statusId == 5L) {
            if (ChronoUnit.DAYS.between(today, publishedProgramDate) < 2 && programGroupSizeMin > updatedGroupSize) {
                publishedProgram.setStatus(statusRepository.findById(2L).get());
            }
            else {
                publishedProgram.setStatus(statusRepository.findById(1L).get());
            }
        }
        publishedProgramRepository.save(publishedProgram);

        PublishedProgram savedPublishedProgram = publishedProgramRepository.findById(guestPublishedProgramUpdateDto.getId()).get();

        return GuestPublishedProgramUpdatedDto.builder()
                .id(savedPublishedProgram.getId())
                .reservationId(reservation.getId())
                .build();
    }
}
