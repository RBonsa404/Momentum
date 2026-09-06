package com.momentum.auth.controller;

import com.momentum.auth.dto.ChangePasswordRequest;
import com.momentum.auth.dto.LoginRequest;
import com.momentum.auth.dto.MeResponse;
import com.momentum.auth.dto.RefreshRequest;
import com.momentum.auth.dto.TokenResponse;
import com.momentum.auth.service.AuthService;
import com.momentum.auth.service.JwtService;
import com.momentum.common.tracing.CorrelationIds;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request.getRefreshToken());
    }

    @GetMapping("/me")
    public MeResponse me(HttpServletRequest request) {
        return authService.me(resolveUserId(request));
    }

    @PostMapping("/change-password")
    public void changePassword(HttpServletRequest request, @Valid @RequestBody ChangePasswordRequest body) {
        authService.changePassword(resolveUserId(request), body);
    }

    private UUID resolveUserId(HttpServletRequest request) {
        String header = request.getHeader(CorrelationIds.USER_HEADER);
        if (header != null && !header.isBlank()) {
            return UUID.fromString(header);
        }
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            return UUID.fromString(jwtService.parse(auth.substring(7)).getSubject());
        }
        throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.UNAUTHORIZED, "Missing user");
    }
}
