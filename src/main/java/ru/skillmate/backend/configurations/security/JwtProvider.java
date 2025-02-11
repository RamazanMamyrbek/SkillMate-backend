package ru.skillmate.backend.configurations.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.accessExpiration}")
    private long accessExpirationTime;
    @Value("${jwt.refreshExpiration}")
    private long refreshExpirationTime;
    private Key key;

    @PostConstruct
    private void postConstruct() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getIssuer().equals("SkillMate");
        } catch (ExpiredJwtException e) {
            log.warn("JWT токен истёк: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Ошибка валидации JWT токена: {}", e.getMessage());
        }
        return false; // Токен недействителен
    }


    public String refreshAccessToken(String refreshToken) {
        if(!validateToken(refreshToken)) {
            throw new JwtException("Invalid refresh token");
        }
        String email = extractUsername(refreshToken);
        return generateAccessToken(email);
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public int getAccessKeyExpiration() {
        return (int) accessExpirationTime / 1000;
    }

    public int getRefreshKeyExpiration() {
        return (int) refreshExpirationTime / 1000;
    }


    public String generateAccessToken(String email) {
        return createToken(email, accessExpirationTime);
    }

    public String generateRefreshToken(String email) {
        return createToken(email, refreshExpirationTime);
    }

    private String createToken(String email, long expirationTime) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiresAt = new Date(issuedAt.getTime() + expirationTime);

        return Jwts
                .builder()
                .setSubject(email)
                .setIssuer("SkillMate")
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(key)
                .compact();
    }

    private Claims parseClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

