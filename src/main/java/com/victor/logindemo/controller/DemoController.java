package com.victor.logindemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/auth-methods")
    public String authMethods() {
        return "auth-methods";
    }
}
