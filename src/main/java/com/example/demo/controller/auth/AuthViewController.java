package com.example.demo.controller.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthViewController {

    private static final Logger log = LoggerFactory.getLogger(AuthViewController.class);

    @GetMapping("/login")
    public String home(Model model) {
        return "/content/authScreen/login";
    }

    @PostMapping("/verifyPhone")
    public String showPhoneAuthPage(
            @RequestParam("phoneNumber") String phoneNumber,
            Model model
    ) {
        model.addAttribute("phoneNumber", phoneNumber);
        return "/content/authScreen/verify-phone";
    }

    @GetMapping("/email-login")
    public String emailLogin(Model model) {
        return "/content/authScreen/email-login";
    }

}
