package com.m2j2.haruseoul.member.service;

import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.member.dto.MemberCreateDto;
import com.m2j2.haruseoul.member.dto.MemberUpdateDto;
import org.springframework.transaction.annotation.Transactional;


public interface MemberService {
    Member save(MemberCreateDto memberCreateDto);
    String validateId(String userId);

    void update(MemberUpdateDto memberUpdateDto);
    Member validatePwd(Long memberId,String currentPwd);

    void delete(Long id);
}