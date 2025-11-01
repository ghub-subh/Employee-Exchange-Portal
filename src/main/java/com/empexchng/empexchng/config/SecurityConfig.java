package com.empexchng.empexchng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
 @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll()
        )
        .formLogin(form -> form.disable()) // You’re using your own login page
        .httpBasic(basic -> basic.disable())
        .csrf(csrf -> csrf.disable()) // 🔥 temporarily disable CSRF for debugging
        .oauth2Login(oauth -> oauth
            .loginPage("/")
            .defaultSuccessUrl("/oauth2/success", true)
        );

    return http.build();
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
