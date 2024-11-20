package com.m2j2.haruseoul.notification.service;

import com.m2j2.haruseoul.notification.dto.NotificationSendDto;

public interface NotificationService {

    void send(NotificationSendDto notificationSendDto);

}
