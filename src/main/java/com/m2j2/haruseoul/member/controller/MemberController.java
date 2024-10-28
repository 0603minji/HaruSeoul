package com.m2j2.haruseoul.member.controller;

import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.member.dto.MemberCreateDto;
import com.m2j2.haruseoul.member.dto.MemberUpdateDto;
import com.m2j2.haruseoul.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("memberController")
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;

    }

    @PostMapping("signup")
    public ResponseEntity<Member> create(@RequestBody MemberCreateDto memberCreateDto) {
        return ResponseEntity.ok(memberService.save(memberCreateDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody MemberUpdateDto memberUpdateDto) {

        Member member = memberService.validatePwd(id, memberUpdateDto.getCurrentPwd());

        memberService.update(member, memberUpdateDto.getNewPwd());

        return ResponseEntity.ok("비밀번호 변경 완료");
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        memberService.delete(id);
    }
}
