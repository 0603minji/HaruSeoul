package com.m2j2.haruseoul.member.service;

import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.member.dto.MemberCreateDto;
import com.m2j2.haruseoul.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultMemberService implements MemberService {

    final MemberRepository memberRepository;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DefaultMemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Member save(MemberCreateDto memberCreateDto) {
        Member member = Member.builder()
                .userId(memberCreateDto.getUserId())
                .userPwd(bCryptPasswordEncoder.encode(memberCreateDto.getUserPwd()))
                .name(memberCreateDto.getName())
                .birth(memberCreateDto.getBirthday())
                .email(memberCreateDto.getEmail())
                .nickname(memberCreateDto.getNickName())
                .build();

        return memberRepository.save(member);
    }
}
