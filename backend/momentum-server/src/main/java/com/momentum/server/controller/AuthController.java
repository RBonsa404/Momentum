package com.momentum.server.controller;

import com.momentum.server.config.SecurityUtils;
import com.momentum.server.dto.auth.ChangePasswordRequest;
import com.momentum.server.dto.auth.LoginRequest;
import com.momentum.server.dto.auth.MeResponse;
import com.momentum.server.dto.auth.RefreshRequest;
import com.momentum.server.dto.auth.TokenResponse;
import com.momentum.server.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public TokenResponse register(@Valid @RequestBody com.momentum.server.dto.auth.RegisterRequest request) {
        return authService.register(request);
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
    public MeResponse me() {
        return authService.me(SecurityUtils.getUserId());
    }

    @PostMapping("/change-password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest body) {
        authService.changePassword(SecurityUtils.getUserId(), body);
    }
}
