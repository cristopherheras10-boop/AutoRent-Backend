package com.autorent.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")String secret;
    @Value("${app.jwt.expiration-ms}")long exp;
    private SecretKey key() {
        if(secret==null || secret.getBytes(StandardCharsets.UTF_8).length<32)
            throw new IllegalStateException("Configure JWT_SECRET con al menos 32 bytes aleatorios.");
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    public String generarToken(String correo,String rol) {
        return Jwts.builder().claims(Map.of("rol",rol)).subject(correo).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+exp)).signWith(key()).compact();
    }
    public String extraerCorreo(String t) {
        return claims(t).getSubject();
    }
    public boolean esValido(String t) {
        try {
            return claims(t).getExpiration().after(new Date());
        } catch(Exception e) {
            return false;
        }
    }
    private Claims claims(String t) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(t).getPayload();
    }
}
