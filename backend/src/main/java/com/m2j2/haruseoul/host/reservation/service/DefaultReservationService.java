package com.m2j2.haruseoul.host.reservation.service;

import com.m2j2.haruseoul.entity.Image;
import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.entity.PublishedProgram;
import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.host.publishedProgram.dto.PublishedProgramListDto;
import com.m2j2.haruseoul.host.reservation.dto.ReservationCancelDto;
import com.m2j2.haruseoul.host.reservation.dto.ReservationListDto;
import com.m2j2.haruseoul.notification.dto.NotificationSendDto;
import com.m2j2.haruseoul.notification.service.NotificationService;
import com.m2j2.haruseoul.repository.ReservationRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service("DefaultHostReservationService")
public class DefaultReservationService implements ReservationService {

    ReservationRepository reservationRepository;
    ModelMapper modelMapper;
    private final NotificationService notificationService;

    public DefaultReservationService(ReservationRepository reservationRepository, ModelMapper modelMapper, NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.modelMapper = modelMapper;
        this.notificationService = notificationService;
    }

    @Override
    public Reservation cancel(Long id, ReservationCancelDto reservationCancelDto) {
        // 예약 ID로 해당 예약을 찾아옵니다.
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        // 삭제 날짜를 현재 시간으로 설정합니다.
        reservation.setDeleteDate(Instant.now());  // 삭제 일자를 현재 시간으로 설정 (LocalDateTime으로 변경 가능)

        // cancelMethod와 cancelReason 설정
        reservation.setCancelMethod(reservationCancelDto.getCancelMethod());
        reservation.setCancelReason(reservationCancelDto.getCancelReason());

        // 예약을 업데이트합니다.
        Reservation saved = reservationRepository.save(reservation);

        // 로그를 남겨서 예약 삭제에 대한 기록을 남길 수 있습니다.
        // System.out.println("Reservation with ID " + reservationId + " has been soft deleted at " + Instant.now());

        //==================알림부분=================================

        Long senderId = saved.getPublishedProgram().getProgram().getMember().getId();
        Long programId = saved.getPublishedProgram().getId();
        NotificationSendDto notificationSendDto = NotificationSendDto.builder()
                .senderId(senderId)
                .programId(programId)
                .type("CANCEL FROM HOST")
                .build();
        notificationService.send(notificationSendDto);

        return saved;
    }

    @Override
    public List<ReservationListDto> getApplicants(Long ppId) {
        modelMapper.typeMap(Reservation.class, ReservationListDto.class)
                .addMappings(mapper -> {
                            mapper.map(src -> src.getMember().getNickname(), ReservationListDto::setMemberNickname); // 예약한 계정
                            mapper.map(src -> src.getMember().getName(), ReservationListDto::setApplicantName); // 실제 참가자 이름
                            mapper.map(src -> src.getMember().getEmail(), ReservationListDto::setEmail);
                            mapper.map(src -> src.getMember().getProfileImgSrc(), ReservationListDto::setGuestProfileImgSrc);
                            mapper.map(src -> src.getPublishedProgram().getProgram().getRequirement(), ReservationListDto::setHostRequirement);

//                    mapper.map(src -> src.getMember().getPhone(), ReservationListDto::setPhone);
                        }
                );

        List<ReservationListDto> reservationListDtos = reservationRepository.findByPpIdByCancelMethod(ppId)
                .stream()
                .map(reservation -> modelMapper.map(reservation, ReservationListDto.class))
                .toList();
        System.out.println("=================================================================================================");
        System.out.println(reservationListDtos);

        return reservationListDtos;
    }
}
