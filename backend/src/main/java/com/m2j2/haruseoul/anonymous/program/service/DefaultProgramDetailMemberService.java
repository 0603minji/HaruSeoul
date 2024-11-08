package com.m2j2.haruseoul.anonymous.program.service;

import com.m2j2.haruseoul.anonymous.program.dto.ProgramDetailMemberDto;
import com.m2j2.haruseoul.repository.MemberRepository;
import com.m2j2.haruseoul.repository.ProgramRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultProgramDetailMemberService implements ProgramDetailMemberService {

    private final ProgramRepository programRepository;
    private final MemberRepository memberRepository;

    public DefaultProgramDetailMemberService(MemberRepository memberRepository, ProgramRepository programRepository) {
        this.memberRepository = memberRepository;
        this.programRepository = programRepository;
    }

    @Override
    public ProgramDetailMemberDto getName(Long id) {
        Long regMemberId = programRepository.findMemberIdByProgramId(id);

        String regMemberName = memberRepository.getNameById(regMemberId);

        ProgramDetailMemberDto programDetailMemberDto
                = ProgramDetailMemberDto.builder().hostName(regMemberName).build();
        return programDetailMemberDto;
    }
}
