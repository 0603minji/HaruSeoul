package com.m2j2.haruseoul.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationSendDto {
    private Long senderId;
    private Long programId;
    private String type;
}
