package com.m2j2.haruseoul.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponseDto {
    private List<NotificationListDto> notificationListDtos;
    private Integer totalCount;
}
