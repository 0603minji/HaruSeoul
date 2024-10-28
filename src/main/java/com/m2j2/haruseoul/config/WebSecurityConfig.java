//package com.m2j2.haruseoul.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .authorizeHttpRequests(
//                        (auth) -> auth
//                                .requestMatchers("/", "/login","/signup","/api/v1/members/signup").permitAll()
//                                .requestMatchers("/api/v1/**").authenticated()
//
//                );
////        http
////                .formLogin(
////                        (auth) -> auth
////                                .loginPage("/login")
//////                                .loginProcessingUrl("/loginProc")
////                                .permitAll()
////                );
//        http
//                .csrf(
//                        AbstractHttpConfigurer::disable
//                );
//
//        return http.build();
//    }
////
////
//}
