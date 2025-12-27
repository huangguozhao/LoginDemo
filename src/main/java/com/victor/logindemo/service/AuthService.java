package com.victor.logindemo.service;

import com.victor.logindemo.dto.LoginRequest;
import com.victor.logindemo.dto.LoginResponse;
import com.victor.logindemo.dto.RegisterRequest;
import com.victor.logindemo.entity.User;
import com.victor.logindemo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);

        if (user == null) {
            return new LoginResponse(false, "用户不存在");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new LoginResponse(false, "密码错误");
        }

        // 创建Session
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setMaxInactiveInterval(30 * 60); // 30分钟过期

        return new LoginResponse(true, "登录成功", user.getUsername(), "/dashboard");
    }

    public boolean register(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return false;
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return false;
        }

        // 检查密码确认
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return false;
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());

        userRepository.save(user);
        return true;
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return null;
        }

        return userRepository.findById(userId).orElse(null);
    }

    public boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("userId") != null;
    }
}
