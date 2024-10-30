package com.m2j2.haruseoul.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateDto {

//    private String id;   // modelMapper를 사용하려면 userId 와 구별못해서 안써도 명시해줘야함 -> mapper.skip으로 처리
    private String name;
    private String userId;
    private String userPwd;
    private String nickname;
    private String email;
    private LocalDate birth;

}
