package com.m2j2.haruseoul.member.service;

import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.member.dto.MemberCreateDto;
import org.springframework.stereotype.Service;


public interface MemberService {

    Member save(MemberCreateDto memberCreateDto);
}
