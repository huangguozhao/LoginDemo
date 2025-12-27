//package com.victor.logindemo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@Order(3)
//public class OAuth2SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain oauth2FilterChain(HttpSecurity http) throws Exception {
//        http
//            .securityMatcher("/oauth2/**")
//            .authorizeHttpRequests(authz -> authz
//                .requestMatchers("/oauth2/login").permitAll()
//                .requestMatchers("/oauth2/**").authenticated()
//                .anyRequest().denyAll()
//            )
//            .oauth2Login(oauth2 -> oauth2
//                .loginPage("/oauth2/login")
//                .defaultSuccessUrl("/oauth2/dashboard", true)
//                .failureUrl("/oauth2/login?error=true")
//            )
//            .logout(logout -> logout
//                .logoutUrl("/oauth2/logout")
//                .logoutSuccessUrl("/oauth2/login?logout=true")
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//            );
//
//        return http.build();
//    }
//}
