package com.m2j2.haruseoul.notification.service;

import com.m2j2.haruseoul.notification.dto.NotificationResponseDto;
import com.m2j2.haruseoul.notification.dto.NotificationSendDto;

import java.util.List;

public interface NotificationService {

    NotificationResponseDto getNotificationResponseDtoList(Long id);
    void sendFromGuest(NotificationSendDto notificationSendDto);
    void sendFromHost(NotificationSendDto notificationSendDto,Long guestId,Long deletedReservationId);
    void confirmNotification(Long id);

}
