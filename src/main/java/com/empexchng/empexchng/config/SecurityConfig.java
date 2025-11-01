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
SecurityFilterChain security(HttpSecurity http) throws Exception {
  http
    .authorizeHttpRequests(auth -> auth
      .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
      .anyRequest().authenticated()
    )

    // OAuth2 login configuration
    .oauth2Login(oauth -> oauth
      .loginPage("/")                       // your custom page
      .defaultSuccessUrl("/oauth2/success", true) // always go here after OAuth2 login
    )
    // Logout configuration (top level, not inside oauth2Login)
    .logout(logout -> logout
      .logoutUrl("/logout")
      .logoutSuccessUrl("/")
      .permitAll()
    );
  return http.build();
}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
