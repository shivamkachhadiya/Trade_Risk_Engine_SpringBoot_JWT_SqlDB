package com.shivam.trade_risk_engine.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "shivam-trade-risk-engine-secret-key-256bit-long";
    private static final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)           // payload mein email daal
                .issuedAt(new Date())   // kab bana token
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))// kab expire hoga — 10 ghante baad
                .signWith(getSigningKey())  // SECRET se sign karo — stamp lagao
                .compact();          // sab compress karke ek string banao
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())// SECRET se verify karo — stamp sahi hai?
                .build()
                .parseSignedClaims(token)    // token kholo
                .getPayload()
                .getSubject();// andar se email nikalo
    }

    public boolean isTokenValid(String token) {
        try {
            extractEmail(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}