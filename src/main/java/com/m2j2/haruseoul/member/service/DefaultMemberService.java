package com.m2j2.haruseoul.member.service;

import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.member.dto.MemberCreateDto;
import com.m2j2.haruseoul.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultMemberService implements MemberService {

    final MemberRepository memberRepository;


    public DefaultMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member save(MemberCreateDto memberCreateDto) {
        Member member = Member.builder()
                .userId(memberCreateDto.getUserId())
                .userPwd(memberCreateDto.getUserPwd())
                .name(memberCreateDto.getName())
                .birth(memberCreateDto.getBirthday())
                .email(memberCreateDto.getEmail())
                .nickname(memberCreateDto.getNickname())
                .build();

        return memberRepository.save(member);
    }
}
