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



        // trim을 적용하여 빈 문자열을 null로 처리
        String inclusion = program.getInclusion() != null ? program.getInclusion().trim().isEmpty() ? null : program.getInclusion().trim() : null;
        String exclusion = program.getExclusion() != null ? program.getExclusion().trim().isEmpty() ? null : program.getExclusion().trim() : null;
        String packingList = program.getPackingList() != null ? program.getPackingList().trim().isEmpty() ? null : program.getPackingList().trim() : null;
        String caution = program.getCaution() != null ? program.getCaution().trim().isEmpty() ? null : program.getCaution().trim() : null;
        String detail = program.getDetail() != null ? program.getDetail().trim().isEmpty() ? null : program.getDetail().trim() : null;

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
                .detail(detail)  // trim 적용
                .inclusion(inclusion)  // trim 적용
                .exclusion(exclusion)  // trim 적용
                .packingList(packingList)  // trim 적용
                .duration(hours)
                .startTime(startTime)
                .caution(caution)  // trim 적용
                .status(program.getStatus())
                .build();

        return programDetailProgramDto;
    }
}
