package com.victor.logindemo.controller;

import com.victor.logindemo.dto.LoginRequest;
import com.victor.logindemo.dto.RegisterRequest;
import com.victor.logindemo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model,
                           HttpServletRequest request) {
        // 如果已经登录，重定向到仪表板
        if (authService.isAuthenticated(request)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("loginRequest", new LoginRequest());

        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }

        if (logout != null) {
            model.addAttribute("message", "已成功退出登录");
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model, HttpServletRequest request) {
        // 如果已经登录，重定向到仪表板
        if (authService.isAuthenticated(request)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        // 检查密码确认
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            model.addAttribute("error", "两次输入的密码不一致");
            return "auth/register";
        }

        boolean success = authService.register(registerRequest);
        if (!success) {
            model.addAttribute("error", "用户名或邮箱已存在");
            return "auth/register";
        }

        redirectAttributes.addFlashAttribute("message", "注册成功，请登录");
        return "redirect:/auth/login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        authService.logout(request);
        redirectAttributes.addFlashAttribute("message", "已成功退出登录");
        return "redirect:/auth/login";
    }
}
