package com.siddardha.redisDemonstration.Util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secret;
    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    public JwtUtil(@Value("${jwt.secret}") String jwtSecret) {
        this.secret = jwtSecret;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username) {
         return Jwts.builder()
                 .setSubject(username)
                 .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                 .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                 .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
      try {
          Jwts.parserBuilder()
                  .setSigningKey(getSigningKey())
                  .build()
                  .parseClaimsJws(token);
          return true;
      } catch (Exception e) {
          return false;
      }
    }
}
