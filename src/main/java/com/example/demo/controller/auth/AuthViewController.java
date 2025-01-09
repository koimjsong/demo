package com.example.demo.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthViewController {

    @GetMapping("/login")
    public String home(Model model) {
        return "/authScreen/login";
    }

    @GetMapping("/verifyPhone")
    public String showPhoneAuthPage() {
        return "/authScreen/verify-phone";
    }

}
