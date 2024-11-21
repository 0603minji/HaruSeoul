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
        PublishedProgram publishedProgram = publishedProgramRepository.findById(guestPublishedProgramUpdateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("publishedProgram not found with ID: " + guestPublishedProgramUpdateDto.getId()));

        // 현재 공개프로그램 총 인원수
        int currentGroupSize = publishedProgram.getGroupSizeCurrent();


        Reservation reservation = reservationRepository.findById(guestPublishedProgramUpdateDto.getReservationId())
                .orElseThrow(() -> new EntityNotFoundException("reservation not found with ID: " + guestPublishedProgramUpdateDto.getReservationId()));

        // 취소하는 예약 인원 수
        int numberOfGuest = reservation.getNumberOfGuest();

        int updatedGroupSize = currentGroupSize - numberOfGuest;
        publishedProgram.setGroupSizeCurrent(updatedGroupSize);

        Long statusId = guestPublishedProgramUpdateDto.getStatusId();

        Long reservationStatusId = guestPublishedProgramUpdateDto.getReservationStatus();

        Status newStatus = null;

        // 공개프로그램의 statusId가 6일때는, 현재 인원이 0명이 되면 상태가 취소됨으로 자동 변경되어야 함
        if (statusId == 6 && updatedGroupSize == 0) {
            newStatus = statusRepository.findById(4L)
                    .orElseThrow(() -> new EntityNotFoundException("Status with ID 4 not found"));
            reservation.setReservationStatus(3);
            reservationRepository.save(reservation);
        }
        else if (statusId == 6 && updatedGroupSize == 1) {
            newStatus = statusRepository.findById(2L)
                    .orElseThrow(() -> new EntityNotFoundException("Status with ID 2 not found"));
        }

        // 상태가 변경되어야 한다면 엔티티의 상태 업데이트
        if (newStatus != null) {
            publishedProgram.setStatus(newStatus);
        }

        Date deleteDate = new Date(); // 현재 시간
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(deleteDate);
        calendar.add(Calendar.HOUR, 9); // 9시간 추가
        deleteDate = calendar.getTime();

        reservation.setDeleteDate(deleteDate.toInstant());

        publishedProgramRepository.save(publishedProgram);

        PublishedProgram savedPublishedProgram = publishedProgramRepository.findById(guestPublishedProgramUpdateDto.getId()).get();

        return GuestPublishedProgramUpdatedDto.builder()
                .id(savedPublishedProgram.getId())
                .statusId(savedPublishedProgram.getStatus().getId())
                .reservationId(reservation.getId())
                .build();
    }
}
