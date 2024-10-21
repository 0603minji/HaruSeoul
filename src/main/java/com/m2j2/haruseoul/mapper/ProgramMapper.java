package com.m2j2.haruseoul.mapper;

import com.m2j2.haruseoul.dto.ProgramDto;
import com.m2j2.haruseoul.entity.Program;

public class ProgramMapper {
    public static Program mapToEntity(ProgramDto programDto) {
        return Program.builder()
                .id(programDto.getId())
                .title(programDto.getTitle())
                .detail(programDto.getDetail())
                .regDate(programDto.getRegDate())
                .endTime(programDto.getEndTime())
                .status(programDto.getStatus())
                .price(programDto.getPrice())
                .groupSizeMin(programDto.getGroupSizeMin())
                .groupSizeMax(programDto.getGroupSizeMax())
                .rating(programDto.getRating())
                .updateDate(programDto.getUpdateDate())
                .language(programDto.getLanguage())
                .startTime(programDto.getStartTime())
                .member(programDto.getMember())
                .images(programDto.getImages())
                .reviews(programDto.getReviews())
                .build();
    }

    public static ProgramDto mapToDto(Program program) {
        return ProgramDto.builder()
                .id(program.getId())
                .title(program.getTitle())
                .detail(program.getDetail())
                .regDate(program.getRegDate())
                .endTime(program.getEndTime())
                .status(program.getStatus())
                .price(program.getPrice())
                .groupSizeMin(program.getGroupSizeMin())
                .groupSizeMax(program.getGroupSizeMax())
                .rating(program.getRating())
                .updateDate(program.getUpdateDate())
                .language(program.getLanguage())
                .startTime(program.getStartTime())
                .member(program.getMember())
                .images(program.getImages())
                .reviews(program.getReviews())
                .build();
    }
}
