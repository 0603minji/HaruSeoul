package com.m2j2.haruseoul.auth.filter;

import com.m2j2.haruseoul.auth.entity.HaruSeoulUserDetails;
import com.m2j2.haruseoul.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDate;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 방법1 : 토큰에 있는 내용을 모두 꺼내서 그것만으로 인증을 처리할건지?
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {

                Long id = jwtUtil.extractUserId(token);
                String userId = jwtUtil.extractUsername(token);
                String name = jwtUtil.extractName(token);
                String nickname = jwtUtil.extractNickname(token);
                String email = jwtUtil.extractEmail(token);
                LocalDate birthday = jwtUtil.extractBirthday(token);
                String profileImgSrc = jwtUtil.extractProfileImgSrc(token);

//                List<String> roles = jwtUtil.extractRoles(token);


                if (userId != null && !(userId.equals(""))) {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                    List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
//                    for (String role : roles) {
//                        authorities.add(new SimpleGrantedAuthority(role));
//
//                    }

                    UserDetails userDetails = HaruSeoulUserDetails.builder()
                            .id(id)
                            .username(userId)
                            .name(name)
                            .email(email)
                            .birth(birthday)
                            .nickname(nickname)
                            .profileImgSrc(profileImgSrc)
//                            .authorities(authorities)
                            .build();

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, null);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }


        }

        filterChain.doFilter(request, response);


        // 방법2 : 토큰에 담을 내용을 최소화하고 여기서 추가적인 인증작업을 할건지


    }
}
