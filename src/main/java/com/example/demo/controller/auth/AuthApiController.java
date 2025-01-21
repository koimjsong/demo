package com.example.demo.controller.auth;

import com.example.demo.Service.auth.JwtService;
import com.example.demo.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Value("${token.expiration.access}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${token.expiration.refresh}")
    private long REFRESH_TOKEN_EXPIRATION;

    private final JwtService jwtService;

    public AuthApiController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // 토큰 검증
    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtService.validateAccessToken(token.replace("Bearer ", ""));
            return ResponseEntity.ok().body(Map.of("success", true, "email", email));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Access Token expired"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid Token"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        try {
            if (jwtService.validateRefreshToken(refreshToken)) {
                String email = jwtService.getEmailFromToken(refreshToken);
                String newAccessToken = jwtService.generateAccessToken(email);

                return ResponseEntity.ok(Map.of("success", true, "accessToken", newAccessToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid Refresh Token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Error refreshing token"));
        }
    }

    // HttpOnly 쿠키로 토근 검증/재발급 처리
    @GetMapping("/protected-resource")
    public ResponseEntity<?> getProtectedResource(@CookieValue("accessToken") String accessToken) {
        try {
            String email = jwtService.validateAccessToken(accessToken);
            return ResponseEntity.ok(Map.of("email", email, "message", "Access granted"));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Access Token expired"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid Token"));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        try {
            String newAccessToken = jwtService.refreshAccessToken(refreshToken);

            // 새 Access Token을 HttpOnly 쿠키로 설정
            ResponseCookie accessTokenCookie = CookieUtil.createCookie("accessToken", newAccessToken, ACCESS_TOKEN_EXPIRATION, false, true);
            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

            return ResponseEntity.ok(Map.of("success", true, "message", "Access Token refreshed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Refresh Token expired or invalid"));
        }
    }

}
