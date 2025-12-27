package com.victor.logindemo.controller;

import com.victor.logindemo.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/jwt")
public class JwtAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService jwtUserDetailsService;

    public JwtAuthController(AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil,
                           UserDetailsService jwtUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "登录失败，请检查用户名和密码");
        }
        return "jwt/login";
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public ResponseEntity<?> authenticate(@RequestParam String username,
                                        @RequestParam String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("USER")
                .replace("ROLE_", "");

            String token = jwtUtil.generateToken(username, role);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", username);
            response.put("role", role);
            response.put("expiresIn", 86400); // 24 hours in seconds

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid credentials");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(value = "token", required = false) String token,
                           Model model) {
        if (token == null || token.trim().isEmpty()) {
            return "redirect:/jwt/login?error=token_required";
        }

        String jwtToken = token;
        String username = jwtUtil.extractUsername(jwtToken);
        String role = jwtUtil.extractRole(jwtToken);

        if (!jwtUtil.validateToken(jwtToken, username)) {
            return "redirect:/jwt/login?error=invalid_token";
        }

        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("token", jwtToken);

        return "jwt/dashboard";
    }

    @GetMapping("/profile")
    public String profile(@RequestParam(value = "token", required = false) String token,
                         Model model) {
        if (token == null || token.trim().isEmpty()) {
            return "redirect:/jwt/login?error=token_required";
        }

        String jwtToken = token;
        String username = jwtUtil.extractUsername(jwtToken);
        String role = jwtUtil.extractRole(jwtToken);

        if (!jwtUtil.validateToken(jwtToken, username)) {
            return "redirect:/jwt/login?error=invalid_token";
        }

        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("token", jwtToken);

        return "jwt/profile";
    }

    @GetMapping("/api/test")
    @ResponseBody
    public ResponseEntity<?> apiTest(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Token required"));
        }

        String jwtToken = token.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);

        if (!jwtUtil.validateToken(jwtToken, username)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "JWT认证成功！");
        response.put("username", username);
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}
