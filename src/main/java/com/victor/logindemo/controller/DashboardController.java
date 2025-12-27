package com.victor.logindemo.controller;

import com.victor.logindemo.entity.User;
import com.victor.logindemo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private AuthService authService;

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        // 检查用户是否已登录
        if (!authService.isAuthenticated(request)) {
            return "redirect:/auth/login";
        }

        User currentUser = authService.getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", currentUser);
        return "dashboard";
    }

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        // 如果已登录，重定向到仪表板
        if (authService.isAuthenticated(request)) {
            return "redirect:/dashboard";
        }
        // 未登录，重定向到登录页面
        return "redirect:/auth/login";
    }
}
