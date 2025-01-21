package com.example.demo.Service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    //private final String secretKey = "rF3#v@e6N!pL$kEwTz4XpQ9jUv8BnMqL";
    private final String SECRET_KEY = Base64.getEncoder().encodeToString("rF3#v@e6N!pL$kEwTz4XpQ9jUv8BnMqL".getBytes(StandardCharsets.UTF_8)); // secretKey 그냥 만듬 테스트니까
//    private final long ACCESS_CODE_VALID_TIME = 1000 * 60 * 15; // 15분
//    private final long REFRESH_TOKEN_VALID_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    @Value("${token.expiration.access}")
    private long ACCESS_CODE_VALID_TIME;

    @Value("${token.expiration.refresh}")
    private long REFRESH_TOKEN_VALID_TIME;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_CODE_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateRefreshToken(String token) {
        try {

            Jwts.parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error validating token: " + e.getMessage());
            return false;
        }
    }

    public String validateAccessToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Invalid Token", e);
        }
    }


    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            System.out.println("Error extracting email from token: " + e.getMessage());
            return null;
        }
    }
}
