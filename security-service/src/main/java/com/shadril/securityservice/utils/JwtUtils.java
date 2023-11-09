package com.shadril.securityservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public final class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-time}")
    private long expirationTime;

    public boolean hasTokenExpired(String token) {
        try {
            Claims claims = getClaims(token);
            Date tokenExpirationDate = claims.getExpiration();
            return tokenExpirationDate.before(new Date());
        } catch (JwtException e) {
            log.error("Error checking if token has expired", e);
            return true;
        }
    }

    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String extractUser(String token) {
        return getClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractUserRoles(String token) {
        Claims claims = getClaims(token);
        try {
            return claims.get("roles", List.class);
        } catch (ClassCastException e) {
            log.error("Error extracting user roles from token", e);
            throw new JwtException("Roles claim is not of expected type List<String>");
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }
}
