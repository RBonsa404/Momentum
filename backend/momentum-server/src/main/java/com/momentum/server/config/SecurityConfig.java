package com.momentum.server.config;

import com.momentum.server.domain.auth.UserAccount;
import com.momentum.server.repository.UserAccountRepository;
import com.momentum.server.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtAuthFilter jwtAuthFilter(JwtService jwtService) {
        return new JwtAuthFilter(jwtService);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/refresh", "/actuator/**").permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
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

    /**
     * Filtre JWT : extrait le userId du token Bearer et le place dans le SecurityContext.
     * Remplace le rôle de l'API Gateway qui injectait X-User-Id.
     */
    public static class JwtAuthFilter extends OncePerRequestFilter {
        private final JwtService jwtService;

        public JwtAuthFilter(JwtService jwtService) {
            this.jwtService = jwtService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    Claims claims = jwtService.parse(token);
                    String userId = claims.getSubject();
                    if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_USER")));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                } catch (JwtException ignored) {
                    // Token invalide → pas d'authentification, la route décidera
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}
