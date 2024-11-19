package com.m2j2.haruseoul.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.m2j2.haruseoul.auth.entity.HaruSeoulUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class JwtUtil {

    private final Key secretKey;
    private final ObjectMapper objectMapper;

    public JwtUtil(@Value("${haruSeoul.jwt.secret}") String secret, ObjectMapper objectMapper) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token); // 유효성 검사를 위해 클레임을 추출
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }
    public String extractName(String token) {
        return extractAllClaims(token).get("name", String.class);
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("id", Long.class);
    }

    public String extractNickname(String token) {
        return extractAllClaims(token).get("nickname", String.class);
    }

    public LocalDate extractBirthday(String token) {
        String birth = extractAllClaims(token).get("birth", String.class);
        return LocalDate.parse(birth, DateTimeFormatter.ISO_DATE);
    }
    public String extractProfileImgSrc(String token) {
        return extractAllClaims(token).get("profileImgSrc", String.class);
    }


//    public List<String> extractRoles(String token) {
//        List<Map<String, String>> roles = extractAllClaims(token).get("roles", List.class);
//
//        List<String> roleNames = new ArrayList<>();
//        for(Map<String, String> role : roles) {
//            System.out.println(role);
//            roleNames.add(role.get("authority"));
//            System.out.println(role.get("authority"));
//        }
//        return roleNames;
//    }

    public Long extractId(String token) {
        return extractAllClaims(token).get("id", Long.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(HaruSeoulUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        claims.put("userId", userDetails.getUsername());
        claims.put("name", userDetails.getName());
        claims.put("email", userDetails.getEmail());
        claims.put("birth", userDetails.getBirth().format(DateTimeFormatter.ISO_DATE));
        claims.put("nickname", userDetails.getNickname());
        claims.put("profileImgSrc", userDetails.getProfileImgSrc());
//        claims.put("roles", userDetails.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

    }
}