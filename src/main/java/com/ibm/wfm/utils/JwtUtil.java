package com.ibm.wfm.utils;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String subject, List<String> bluegroups, long expirationMillis) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("bluegroups", bluegroups)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public List<String> getBluegroups(String token) {
        return (List<String>) validateToken(token).getBody().get("bluegroups");
    }

    public String getUsername(String token) {
        return validateToken(token).getBody().getSubject();
    }
}