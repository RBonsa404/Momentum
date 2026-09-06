package com.momentum.auth.config;

import com.momentum.auth.domain.UserAccount;
import com.momentum.auth.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/refresh", "/actuator/**").permitAll()
                        .anyRequest().permitAll());
        return http.build();
    }

    @Bean
    ApplicationRunner bootstrapUser(
            UserAccountRepository users,
            PasswordEncoder encoder,
            @Value("${momentum.bootstrap.email:you@momentum.local}") String email,
            @Value("${momentum.bootstrap.password:ChangeMeNow!}") String password) {
        return args -> users.findByEmailIgnoreCase(email).orElseGet(() -> {
            UserAccount user = new UserAccount();
            user.setEmail(email.toLowerCase());
            user.setPasswordHash(encoder.encode(password));
            user.setDisplayName("Momentum");
            user.setLocked(false);
            return users.save(user);
        });
    }
}
