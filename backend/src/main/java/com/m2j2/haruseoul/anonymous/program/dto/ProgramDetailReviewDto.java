package com.m2j2.haruseoul.anonymous.program.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramDetailReviewDto {

    private List<ReviewDetailDto> reviewDetailDtos;
    private Integer ratingCount;
    private Integer hostRating;
    private Integer hostRatingCount;
}