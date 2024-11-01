package com.m2j2.haruseoul.member.service;

import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.member.dto.MemberCreateDto;
import com.m2j2.haruseoul.member.dto.MemberListDto;
import com.m2j2.haruseoul.member.dto.MemberUpdateDto;
import com.m2j2.haruseoul.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DefaultMemberService implements MemberService {

    final MemberRepository memberRepository;
    final ModelMapper modelMapper;


    public DefaultMemberService(MemberRepository memberRepository, ModelMapper modelMapper) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Member save(MemberCreateDto memberCreateDto) {

        modelMapper.typeMap(MemberCreateDto.class,Member.class).addMappings(
                modelMapper ->{
                    modelMapper.skip(Member::setId);  // userId와 id가 헷갈리지않게 스킵설정
                }
        );

        Member member = modelMapper.map(memberCreateDto, Member.class);

        return memberRepository.save(member);
    }

    @Override
    public String validateId(String userId) {
        if (memberRepository.existsByUserId(userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT); // 중복된 경우 409 에러 전송
        }
        return "아이디확인필요";
    }


    @Override
    public MemberListDto getList(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        return modelMapper.map(member, MemberListDto.class);
    }


    @Override
    @Transactional
    public void update(MemberUpdateDto memberUpdateDto) {

        Member member = memberRepository.findById(memberUpdateDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        member.setUserPwd(memberUpdateDto.getNewPwd());
        memberRepository.save(member);
    }

    @Override
    public Member validatePwd(Long memberId, String currentPwd) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        if (!currentPwd.equals(member.getUserPwd())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    @Override
    public void delete(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID가 존재하지 않습니다. 이미 삭제되었거나 없는 계정입니다.");
        }
        memberRepository.deleteById(id);
    }

}
