package com.m2j2.haruseoul.member.controller;

import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.member.dto.SigninDto;
import com.m2j2.haruseoul.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("signin")
public class SigninController {

    private final MemberService memberService;

    public SigninController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<?> signin(@RequestBody SigninDto signinDto, HttpSession session) {


        Member member = memberService.signin(signinDto);

        if (member != null) {
            session.setAttribute("userId", member.getId()); // 세션에 사용자 ID 저장
            Long userId = (Long) session.getAttribute("userId");
            System.out.println(userId);

            return ResponseEntity.ok("로그인 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }



}
