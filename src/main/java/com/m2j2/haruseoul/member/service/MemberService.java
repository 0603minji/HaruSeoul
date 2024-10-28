package com.m2j2.haruseoul.member.service;

import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.member.dto.MemberCreateDto;
import com.m2j2.haruseoul.member.dto.MemberUpdateDto;
import org.springframework.transaction.annotation.Transactional;


public interface MemberService {
    Member save(MemberCreateDto memberCreateDto);

    void update(MemberUpdateDto memberUpdateDto);

    void delete(Long id);
}
