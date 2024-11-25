package com.m2j2.haruseoul.host.publishedProgram.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.m2j2.haruseoul.entity.Image;
import com.m2j2.haruseoul.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishedProgramListDto {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant regDate;
    private Long programId;
    private String programTitle;
    private Integer groupSizeMax;
    private Integer groupSizeMin;
    private Integer groupSizeCurrent;
    private LocalDate date;
    private Long statusId;
    private String statusName;
    private List<Image> images;
    private List<Long> reservationIds;
    private List<String> guestProfileImgSrcs;
}
