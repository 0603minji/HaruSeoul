package com.m2j2.haruseoul.anonymous.program.service;

import com.m2j2.haruseoul.anonymous.program.dto.ProgramDetailProgramDto;
import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.repository.ProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;

@Service("programDetailService")
public class DefaultProgramDetailService implements ProgramDetailService {

    private final ProgramRepository programRepository;

    public DefaultProgramDetailService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    @Transactional
    public ProgramDetailProgramDto getDetail(Long programId) {
        Program program = programRepository.findById(programId).orElse(null);

        LocalTime startTime = program.getStartTime();
        LocalTime endTime = program.getEndTime();
        Duration duration = Duration.between(startTime, endTime);
        Float hours = Math.round((duration.toMinutes() / 60.0f) * 10) / 10.0f;



        ProgramDetailProgramDto programDetailProgramDto
                = ProgramDetailProgramDto
                .builder()
                .id(programId)
                .title(program.getTitle())
                .rating(program.getRating())
                .price(program.getPrice())
                .language(program.getLanguage())
                .groupSizeMin(program.getGroupSizeMin())
                .groupSizeMax(program.getGroupSizeMax())
                .detail(program.getDetail())
                .inclusion(program.getInclusion())
                .exclusion(program.getExclusion())
                .packingList(program.getPackingList())
                .duration(hours)
                .startTime(startTime)
                .caution(program.getCaution())
                .build();
        return programDetailProgramDto;
    }
}
