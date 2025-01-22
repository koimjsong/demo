package com.example.demo.controller.auth;

import com.example.demo.Service.auth.MailService;
import com.example.demo.model.EmailVerificationResult;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/email")
public class MailApiController {

    private final MailService mailService;

    public MailApiController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send-code")
    public ResponseEntity sendMessage(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        mailService.sendCodeToEmail(email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verificationEmail(
            @RequestParam("email") @Valid String email,
            @RequestParam("code") String authCode,
            HttpServletResponse response) {
        // 인증번호 검증
        EmailVerificationResult verificationResult = mailService.verifiedCode(email, authCode);

        if (verificationResult.isSuccess()) {
            // 토큰 생성 및 쿠키 추가
            mailService.generateTokens(email, response);

            // 성공 리다이렉트 URL 추가
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("redirectUrl", "/home");
            return ResponseEntity.ok(successResponse);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "인증에 실패했습니다. 인증번호를 다시 확인하세요.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
