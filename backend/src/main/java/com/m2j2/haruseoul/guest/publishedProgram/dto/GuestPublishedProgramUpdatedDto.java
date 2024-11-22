package com.m2j2.haruseoul.guest.publishedProgram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestPublishedProgramUpdatedDto {
    private Long id;
    private Long reservationId;
}
