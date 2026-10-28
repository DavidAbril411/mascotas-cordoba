package ar.edu.ubp.das.mascotasrest.config;

import ar.edu.ubp.das.mascotasrest.BE.UsuarioBE;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${security.jwt-secret}")
    private String secret;

    @Value("${security.jwt-expiration}")
    private long expiration;

    public String crearToken(UsuarioBE usuario) {
        return Jwts.builder()
                .subject(usuario.getCuil())
                .claim("idRefugio", usuario.getIdRefugio())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    public Claims leerToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

}
