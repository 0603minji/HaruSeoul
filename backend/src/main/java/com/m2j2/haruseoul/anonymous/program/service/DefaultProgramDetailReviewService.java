package com.m2j2.haruseoul.anonymous.program.service;

import com.m2j2.haruseoul.anonymous.program.dto.ProgramDetailReviewDto;
import com.m2j2.haruseoul.anonymous.program.dto.ReviewDetailDto;
import com.m2j2.haruseoul.entity.Review;
import com.m2j2.haruseoul.repository.MemberRepository;
import com.m2j2.haruseoul.repository.ProgramRepository;
import com.m2j2.haruseoul.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service("programDetailReviewService")
public class DefaultProgramDetailReviewService implements ProgramDetailReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ProgramRepository programRepository;

    public DefaultProgramDetailReviewService(MemberRepository memberRepository, ReviewRepository reviewRepository, ProgramRepository programRepository) {
        this.memberRepository = memberRepository;
        this.reviewRepository = reviewRepository;
        this.programRepository = programRepository;
    }

    @Override
    @Transactional
    public ProgramDetailReviewDto getList(Long programId) {
        List<Review> reviews = reviewRepository.findByProgramId(programId);

        List<ReviewDetailDto> reviewDetailDtos = reviews.stream().map(review -> {
            String memberName = memberRepository.findNameById(review.getMember().getId());
            return ReviewDetailDto.builder()
                    .regMemberName(memberName)
                    .regDate(review.getRegDate().atZone(ZoneId.systemDefault()).toLocalDate())
                    .content(review.getContent())
                    .rating(review.getRating())
                    .build();
        }).collect(Collectors.toList());

        Integer ratingCount = reviews.size();

        Long memberId = programRepository.findMemberIdByProgramId(programId);
        List<Long> programIds = programRepository.findIdByMemberId(memberId);
        List<Review> allReviews = reviewRepository.findByProgramIdIn(programIds);

        Integer hostRating = (int) Math.round(
                allReviews.stream()
                        .mapToDouble(Review::getRating)
                        .average()
                        .orElse(0)
        );

        Integer hostRatingCount = allReviews.size();

        return ProgramDetailReviewDto.builder()
                .reviewDetailDtos(reviewDetailDtos)
                .ratingCount(ratingCount)
                .hostRating(hostRating)
                .hostRatingCount(hostRatingCount)
                .build();
    }
}
