package com.example.demo.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeViewController {

    @GetMapping("")
    public String home(Model model) {
        return "/content/home/home";
    }

}
