package com.m2j2.haruseoul.notification.service;

import com.m2j2.haruseoul.notification.dto.NotificationResponseDto;
import com.m2j2.haruseoul.notification.dto.NotificationSendDto;

import java.util.List;

public interface NotificationService {

    NotificationResponseDto getNotificationResponseDtoList(Long id);
    void send(NotificationSendDto notificationSendDto);
    void confirmNotification(Long id);

}
