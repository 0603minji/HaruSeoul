package com.m2j2.haruseoul.host.publishedProgram.dto;

import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.entity.Reservation;
import com.m2j2.haruseoul.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishedProgramUpdatedDto {
    private Long id;
    private Long programId;
    private Integer groupSizeCurrent;
    private LocalDate date;
    private Long statusId;
    private String statusName;
    private List<Long> bookingMemberIds;
}