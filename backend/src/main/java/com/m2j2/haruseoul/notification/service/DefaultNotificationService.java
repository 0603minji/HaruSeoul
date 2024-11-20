package com.m2j2.haruseoul.notification.service;

import com.m2j2.haruseoul.entity.Notification;
import com.m2j2.haruseoul.notification.config.SseClientRegistry;
import com.m2j2.haruseoul.notification.dto.NotificationSendDto;
import com.m2j2.haruseoul.repository.NotificationRepository;
import com.m2j2.haruseoul.repository.ProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProgramRepository programRepository;
    private final SseClientRegistry sseClientRegistry;

    public DefaultNotificationService(NotificationRepository notificationRepository, ProgramRepository programRepository, SseClientRegistry sseClientRegistry) {
        this.notificationRepository = notificationRepository;
        this.programRepository = programRepository;
        this.sseClientRegistry = sseClientRegistry;
    }

    @Override
    @Transactional
    public void send(NotificationSendDto notificationSendDto) {

        Long programRegMemberId = programRepository.findMemberIdByProgramId(notificationSendDto.getProgramId());

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
                        .data(notification));
                System.out.println("알림 전송 성공: " + notification);
            } catch (IOException e) {
                sseClientRegistry.removeClient(programRegMemberId);
                System.out.println("알림 전송 실패: " + e.getMessage());
            }
        } else {
            System.out.println("SseEmitter가 존재하지 않음: userId=" + programRegMemberId);
        }
    }
}
