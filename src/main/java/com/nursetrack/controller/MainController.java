package com.nursetrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        //return "redirect:/dashboard/index";
        return "index";

    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}