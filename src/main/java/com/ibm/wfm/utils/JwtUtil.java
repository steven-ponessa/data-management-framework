package com.ibm.wfm.utils;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

    private final SecretKey key = Jwts.SIG.HS256.key().build();

    public String generateToken(String subject, List<String> bluegroups, long expirationMillis) {
        return Jwts.builder()
                .subject(subject)
                .claim("bluegroups", bluegroups)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    @SuppressWarnings("unchecked")
    public List<String> getBluegroups(String token) {
        return (List<String>) validateToken(token).getPayload().get("bluegroups");
    }

    public String getUsername(String token) {
        return validateToken(token).getPayload().getSubject();
    }
}