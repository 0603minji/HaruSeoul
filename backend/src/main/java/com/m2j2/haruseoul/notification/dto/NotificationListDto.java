package com.m2j2.haruseoul.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationListDto {
    private Long notificationId;
    private Long programId;
    private Long reservationId;
    private String title;
    private String type;
    private LocalDate regDate;
}
