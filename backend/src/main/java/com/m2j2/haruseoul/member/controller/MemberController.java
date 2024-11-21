package com.m2j2.haruseoul.member.controller;

import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.member.dto.MemberCreateDto;
import com.m2j2.haruseoul.member.dto.MemberIdValidDto;
import com.m2j2.haruseoul.member.dto.MemberListDto;
import com.m2j2.haruseoul.member.dto.MemberUpdateDto;
import com.m2j2.haruseoul.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController("memberController")
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;

    }

    @GetMapping("{id}")
    public ResponseEntity<MemberListDto> getList(@PathVariable Long id){
        return ResponseEntity.ok(memberService.getList(id));
    }

    @PostMapping("idvalid")
    public ResponseEntity<Void> idvalid(@RequestBody MemberIdValidDto idValidDto) {

        memberService.validateId(idValidDto.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("signup")
    public ResponseEntity<Member> create(@RequestBody MemberCreateDto memberCreateDto) {
//        if (!memberCreateDto.getIsChecked()) { // 중복 확인 여부 검사
//            throw new IllegalArgumentException("아이디 중복 확인이 필요합니다.");
//        }
        return ResponseEntity.ok(memberService.save(memberCreateDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @RequestPart(value = "memberUpdateDto",required = false) MemberUpdateDto memberUpdateDto,
                                         @RequestPart(value = "profileImgSrc" ,required = false) MultipartFile profileImgSrc) {

        memberUpdateDto.setId(id);
        memberService.update(memberUpdateDto,profileImgSrc);

        return ResponseEntity.ok("개인정보 변경 완료");
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
