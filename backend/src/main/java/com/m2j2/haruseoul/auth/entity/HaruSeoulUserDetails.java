package com.m2j2.haruseoul.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HaruSeoulUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String name;
    private LocalDate birth;
    private String profileImgSrc;

    private Collection<? extends GrantedAuthority> authorities;
}
