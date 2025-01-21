package com.example.demo.controller.auth;

import com.example.demo.Service.auth.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

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

}
