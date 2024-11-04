package com.m2j2.haruseoul.member.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SigninDto {

    private String userId;
    private String userPwd;

}
