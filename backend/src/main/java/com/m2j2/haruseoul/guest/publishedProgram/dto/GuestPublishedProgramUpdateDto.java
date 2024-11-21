package com.m2j2.haruseoul.guest.publishedProgram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestPublishedProgramUpdateDto {
    private Long id;
    private Long statusId;
    private Long reservationId;
    private Long reservationStatus;
}
