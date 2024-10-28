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

    private String name;
    private String userId;
    private String userPwd;
    private String nickname;
    private String email;
    private LocalDate birthday;

}
