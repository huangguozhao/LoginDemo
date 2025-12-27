package com.victor.logindemo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth2")
public class OAuth2AuthController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "OAuth2登录失败，请重试");
        }
        if (logout != null) {
            model.addAttribute("message", "已成功退出登录");
        }
        return "oauth2/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal OAuth2User oauth2User,
                           OAuth2AuthenticationToken authentication,
                           Model model) {
        if (oauth2User == null) {
            return "redirect:/oauth2/login?error=true";
        }

        String registrationId = authentication.getAuthorizedClientRegistrationId();
        model.addAttribute("provider", registrationId);
        model.addAttribute("name", oauth2User.getAttribute("name"));
        model.addAttribute("email", oauth2User.getAttribute("email"));
        model.addAttribute("login", oauth2User.getAttribute("login")); // GitHub
        model.addAttribute("id", oauth2User.getAttribute("id"));
        model.addAttribute("avatarUrl", oauth2User.getAttribute("avatar_url")); // GitHub
        model.addAttribute("picture", oauth2User.getAttribute("picture")); // Google
        model.addAttribute("attributes", oauth2User.getAttributes());

        return "oauth2/dashboard";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OAuth2User oauth2User,
                         OAuth2AuthenticationToken authentication,
                         Model model) {
        if (oauth2User == null) {
            return "redirect:/oauth2/login?error=true";
        }

        String registrationId = authentication.getAuthorizedClientRegistrationId();
        model.addAttribute("provider", registrationId);
        model.addAttribute("name", oauth2User.getAttribute("name"));
        model.addAttribute("email", oauth2User.getAttribute("email"));
        model.addAttribute("login", oauth2User.getAttribute("login"));
        model.addAttribute("id", oauth2User.getAttribute("id"));
        model.addAttribute("attributes", oauth2User.getAttributes());

        return "oauth2/profile";
    }
}
