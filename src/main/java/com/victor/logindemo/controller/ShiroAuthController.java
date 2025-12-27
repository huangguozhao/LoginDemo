package com.victor.logindemo.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shiro")
public class ShiroAuthController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }
        if (logout != null) {
            model.addAttribute("message", "已成功退出登录");
        }
        return "shiro/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                       @RequestParam String password,
                       @RequestParam(defaultValue = "false") boolean rememberMe) {
        Subject subject = SecurityUtils.getSubject();

        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
            subject.login(token);

            return "redirect:/shiro/dashboard";
        } catch (AuthenticationException e) {
            return "redirect:/shiro/login?error=true";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Subject subject = SecurityUtils.getSubject();

        model.addAttribute("username", subject.getPrincipal());
        model.addAttribute("authenticated", subject.isAuthenticated());
        model.addAttribute("hasRoleAdmin", subject.hasRole("admin"));
        model.addAttribute("hasRoleUser", subject.hasRole("user"));
        model.addAttribute("hasPermissionRead", subject.isPermitted("user:read"));
        model.addAttribute("hasPermissionWrite", subject.isPermitted("user:write"));
        model.addAttribute("hasPermissionManage", subject.isPermitted("admin:manage"));

        return "shiro/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Subject subject = SecurityUtils.getSubject();

        model.addAttribute("username", subject.getPrincipal());
        model.addAttribute("authenticated", subject.isAuthenticated());
        model.addAttribute("roles", subject.hasRole("admin") ? "管理员" : "普通用户");
        model.addAttribute("permissions", subject.isPermitted("user:write") ? "读写权限" : "只读权限");

        return "shiro/profile";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        Subject subject = SecurityUtils.getSubject();

        if (!subject.hasRole("admin")) {
            return "redirect:/shiro/dashboard?error=access_denied";
        }

        model.addAttribute("username", subject.getPrincipal());
        return "shiro/admin";
    }

    @PostMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return "redirect:/shiro/login?logout=true";
    }
}
