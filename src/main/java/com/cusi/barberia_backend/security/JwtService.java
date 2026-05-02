package com.cusi.barberia_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Servicio JWT usando JJWT 0.12.x.
 * En 0.12.x: Jwts.builder() / Jwts.parser() reemplaza a las APIs deprecadas
 * de versiones anteriores (parserBuilder, signWith con algoritmo separado, etc.)
 */
@Component
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /** Genera un token para el admin autenticado. El subject es su nombreUsuario. */
    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    /** Extrae el subject (nombreUsuario) del token. Lanza excepción si es inválido. */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /** Valida firma y expiración. Retorna false si el token es inválido o expiró. */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}