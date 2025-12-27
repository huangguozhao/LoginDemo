package com.victor.logindemo.service;

import com.victor.logindemo.entity.User;
import com.victor.logindemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 获取用户角色
        String[] roles = user.getRoles() != null ?
            user.getRoles().stream()
                .map(role -> role.getRoleCode())
                .toArray(String[]::new) :
            new String[]{"USER"};

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(roles)
            .disabled(user.getStatus() != null && user.getStatus() == 0)
            .accountLocked(isAccountLocked(user))
            .build();
    }

    private boolean isAccountLocked(User user) {
        if (user.getLockedUntil() == null) {
            return false;
        }
        return user.getLockedUntil().isAfter(java.time.LocalDateTime.now());
    }
}
