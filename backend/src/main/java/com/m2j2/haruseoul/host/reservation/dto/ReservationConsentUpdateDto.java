package com.m2j2.haruseoul.host.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationConsentUpdateDto {
    private Long id;
    private Integer guestConsent;
}
