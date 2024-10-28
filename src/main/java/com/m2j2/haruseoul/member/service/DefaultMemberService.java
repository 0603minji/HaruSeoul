package com.m2j2.haruseoul.member.service;

import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.member.dto.MemberCreateDto;
import com.m2j2.haruseoul.member.dto.MemberUpdateDto;
import com.m2j2.haruseoul.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public void update(MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(memberUpdateDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        if(!memberUpdateDto.getCurrentPwd().equals(member.getUserPwd())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다");
        }
        member.setUserPwd(memberUpdateDto.getNewPwd());
        memberRepository.save(member);
    }


    @Override
    public void delete(Long id) {
        memberRepository.deleteById(id);

    }

}
