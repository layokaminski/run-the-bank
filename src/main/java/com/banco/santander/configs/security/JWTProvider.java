package com.banco.santander.configs.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTProvider {

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJWT(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getDocument()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getDocumentJWT(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJWT(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody();

            return true;
        } catch (SignatureException signatureException) {
            throw new RuntimeException("Invalid JWT signature", signatureException);
        } catch (MalformedJwtException malformedJwtException) {
            throw new RuntimeException("Invalid JWT token", malformedJwtException);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new RuntimeException("JWT token is expired", expiredJwtException);
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new RuntimeException("JWT token is unsupported", unsupportedJwtException);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new RuntimeException("JWT claims string is empty", illegalArgumentException);
        }
    }
}