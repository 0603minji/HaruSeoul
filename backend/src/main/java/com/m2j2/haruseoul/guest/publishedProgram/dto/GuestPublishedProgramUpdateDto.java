package com.m2j2.haruseoul.guest.publishedProgram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestPublishedProgramUpdateDto {
    private Long id;
    private Long reservationId;
}
