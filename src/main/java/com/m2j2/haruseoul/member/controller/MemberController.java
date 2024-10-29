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

        memberUpdateDto.setId(id);
        memberService.update(memberUpdateDto);

        return ResponseEntity.ok("비밀번호 변경 완료");
    }

    @PostMapping("checkPwd")
    public ResponseEntity<String> validate(@RequestBody MemberUpdateDto memberUpdateDto) {
        memberService.validatePwd(memberUpdateDto.getId(),memberUpdateDto.getCurrentPwd());
        return ResponseEntity.ok("비밀번호가 일치합니다");
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        memberService.delete(id);
    }
}
