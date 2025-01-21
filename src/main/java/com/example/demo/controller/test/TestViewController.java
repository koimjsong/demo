package com.example.demo.controller.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestViewController {
    private static final Logger log = LoggerFactory.getLogger(TestViewController.class);

    @GetMapping("")
    public String home(Model model) {
        return "content/test/test";
    }

    @GetMapping("/flatpickr")
    public String flatpickr(Model model) {
        return "content/test/flatpickr";
    }

    @GetMapping("/toast")
    public String toast(Model model) {
        return "content/test/toastUi";
    }
}
