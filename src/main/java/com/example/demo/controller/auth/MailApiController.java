package com.example.demo.controller.auth;

import com.example.demo.Service.auth.MailService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Map<String, Object>> verificationEmail(@RequestParam("email") @Valid String email,
                                                                 @RequestParam("code") String authCode,
                                                                 HttpServletResponse response) {
        Map<String, Object> responseMap = mailService.verifyEmailAndGenerateTokens(email, authCode, response);

        if ((boolean) responseMap.get("success")) {
            return ResponseEntity.ok(responseMap);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }
    }
}
