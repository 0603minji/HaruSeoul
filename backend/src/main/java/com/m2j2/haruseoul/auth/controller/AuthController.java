package com.m2j2.haruseoul.auth.controller;
import com.m2j2.haruseoul.auth.dto.SigninDto;
import com.m2j2.haruseoul.auth.dto.SigninResponseDto;
import com.m2j2.haruseoul.auth.entity.HaruSeoulUserDetails;
import com.m2j2.haruseoul.auth.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("authController")
@RequestMapping("auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("signin")
    public ResponseEntity<?> sigin(@RequestBody SigninDto signinDto) {

        System.out.println(signinDto);

        String userId = signinDto.getUserId();
        String userPwd = signinDto.getUserPwd();

        UsernamePasswordAuthenticationToken auth
                = new UsernamePasswordAuthenticationToken(userId, userPwd);


        try {
            // 인증 수행
            Authentication authentication = authenticationManager.authenticate(auth);
            // UserDetailsService를 통해서 사용자 정보를 받아서 인증을 처리한 후
            // 인증에 성공하게 되면 SecurityContextHolder에 사용자 인증 정보를 담아 놓게 됨.

            // 인증 성공 시 사용자 정보 가져오기
            HaruSeoulUserDetails userDetails = (HaruSeoulUserDetails) authentication.getPrincipal();

            String token = jwtUtil.generateToken(userDetails); // 여기에 인증 상태

            // JWT 토큰만 반환
            return ResponseEntity.ok(SigninResponseDto
                    .builder()
                    .memberId(userDetails.getId())
                    .token(token) // 여기에 인증 상태 정보를 모두 담아서 전달함
                    .build());

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");

        }
    }
}
