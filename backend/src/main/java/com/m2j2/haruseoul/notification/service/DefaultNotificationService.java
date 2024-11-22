package com.m2j2.haruseoul.notification.service;

import com.m2j2.haruseoul.entity.*;
import com.m2j2.haruseoul.notification.config.SseClientRegistry;
import com.m2j2.haruseoul.notification.dto.NotificationListDto;
import com.m2j2.haruseoul.notification.dto.NotificationResponseDto;
import com.m2j2.haruseoul.notification.dto.NotificationSendDto;
import com.m2j2.haruseoul.repository.NotificationRepository;
import com.m2j2.haruseoul.repository.ProgramRepository;
import com.m2j2.haruseoul.repository.PublishedProgramRepository;
import com.m2j2.haruseoul.repository.ReservationRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProgramRepository programRepository;
    private final PublishedProgramRepository publishedProgramRepository;
    private final ReservationRepository reservationRepository;
    private final SseClientRegistry sseClientRegistry;

    public DefaultNotificationService(NotificationRepository notificationRepository, ProgramRepository programRepository, PublishedProgramRepository publishedProgramRepository, ReservationRepository reservationRepository, SseClientRegistry sseClientRegistry) {
        this.notificationRepository = notificationRepository;
        this.programRepository = programRepository;
        this.publishedProgramRepository = publishedProgramRepository;
        this.reservationRepository = reservationRepository;
        this.sseClientRegistry = sseClientRegistry;
    }

    @Override
    @Transactional
    public NotificationResponseDto getNotificationResponseDtoList(Long id) {

        List<NotificationListDto> notificationListDtos = new ArrayList<>();


        List<Notification> notifications = notificationRepository.findUnreadListById(id);
        for (Notification notification : notifications) {

            String title = "";

            if(notification.getProgramId() != null) {
                Long programId = publishedProgramRepository.findProgramIdByPublishedProgramId(notification.getProgramId());
                Program program = programRepository.findTitleById(programId);
                title = program.getTitle();
            }
            else if(notification.getReservationId() != null) {
                Long reservationId = notification.getReservationId();
                Reservation reservation = reservationRepository.findById(reservationId).get();
                Long programIdGetByReservation = reservation.getPublishedProgram().getProgram().getId();
                title = programRepository.findTitleById(programIdGetByReservation).getTitle();

            }

            NotificationListDto notificationListDto = NotificationListDto.builder()
                    .notificationId(notification.getId())
                    .programId(notification.getProgramId())
                    .reservationId(notification.getReservationId())
                    .title(title)
                    .type(notification.getType())
                    .regDate(LocalDate.from(notification.getRegDate()))
                    .build();
            notificationListDtos.add(notificationListDto);
        }

        Integer totalCount = notifications.size();

        NotificationResponseDto notificationResponseDto = NotificationResponseDto.builder()
                .notificationListDtos(notificationListDtos)
                .totalCount(totalCount)
                .build();

        return notificationResponseDto;
    }

    @Override
    @Transactional
    public void sendFromGuest(NotificationSendDto notificationSendDto) {

        Long programId = publishedProgramRepository.findProgramIdByPublishedProgramId(notificationSendDto.getProgramId());
        Program program = programRepository.findById(programId).get();
        Long programRegMemberId = program.getMember().getId();

        Notification notification = Notification.builder()
                .senderId(notificationSendDto.getSenderId())
                .programId(notificationSendDto.getProgramId())
                .receiverId(programRegMemberId)
                .type(notificationSendDto.getType())
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        SseEmitter emitter = sseClientRegistry.getClient(programRegMemberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notification, MediaType.APPLICATION_JSON));
                System.out.println("알림 전송 성공: " + notification);
            } catch (IOException e) {
                sseClientRegistry.removeClient(programRegMemberId);
                System.out.println("알림 전송 실패: " + e.getMessage());
            }
        } else {
            System.out.println("SseEmitter가 존재하지 않음: userId=" + programRegMemberId);
        }
    }

    @Override
    @Transactional
    public void sendFromHost(NotificationSendDto notificationSendDto,Long guestId,Long deletedReservationId) {

        Long programId = publishedProgramRepository.findProgramIdByPublishedProgramId(notificationSendDto.getProgramId());
        Program program = programRepository.findById(programId).get();
        Long programRegMemberId = program.getMember().getId();

        Notification notification = Notification.builder()
                .senderId(notificationSendDto.getSenderId())
                .receiverId(guestId)
                .reservationId(deletedReservationId)
                .type(notificationSendDto.getType())
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        SseEmitter emitter = sseClientRegistry.getClient(programRegMemberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notification, MediaType.APPLICATION_JSON));
                System.out.println("알림 전송 성공: " + notification);
            } catch (IOException e) {
                sseClientRegistry.removeClient(programRegMemberId);
                System.out.println("알림 전송 실패: " + e.getMessage());
            }
        } else {
            System.out.println("SseEmitter가 존재하지 않음: userId=" + programRegMemberId);
        }



    }

    @Override
    public void confirmNotification(Long id) {

        Notification notification = notificationRepository.findById(id).get();
        notification.setIsRead(true);
        notificationRepository.save(notification);

    }
}
