package com.momentum.server.service;

import com.momentum.server.domain.auth.RefreshToken;
import com.momentum.server.domain.auth.UserAccount;
import com.momentum.server.dto.auth.ChangePasswordRequest;
import com.momentum.server.dto.auth.LoginRequest;
import com.momentum.server.dto.auth.MeResponse;
import com.momentum.server.dto.auth.TokenResponse;
import com.momentum.server.repository.RefreshTokenRepository;
import com.momentum.server.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class AuthService {
    private static final int MAX_FAILURES = 5;
    private static final int LOCK_MINUTES = 15;

    private final UserAccountRepository users;
    private final RefreshTokenRepository refreshTokens;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final long refreshDays;
    private final SecureRandom random = new SecureRandom();

    public AuthService(
            UserAccountRepository users,
            RefreshTokenRepository refreshTokens,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Value("${momentum.jwt.refresh-days:7}") long refreshDays) {
        this.users = users;
        this.refreshTokens = refreshTokens;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshDays = refreshDays;
    }

    @Transactional
    public TokenResponse register(com.momentum.server.dto.auth.RegisterRequest request) {
        if (users.findByEmailIgnoreCase(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        UserAccount user = new UserAccount();
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName().trim());
        user.setLocked(false);
        user.setFailedAttempts(0);
        UserAccount saved = users.save(user);
        return issuePair(saved);
    }

    @Transactional(noRollbackFor = ResponseStatusException.class)
    public TokenResponse login(LoginRequest request) {
        UserAccount user = users.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (user.isLocked() && user.getLockedUntil() != null && user.getLockedUntil().isAfter(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.LOCKED, "Account locked");
        }
        if (user.isLocked() && user.getLockedUntil() != null && user.getLockedUntil().isBefore(Instant.now())) {
            user.setLocked(false);
            user.setFailedAttempts(0);
            user.setLockedUntil(null);
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            if (user.getFailedAttempts() >= MAX_FAILURES) {
                user.setLocked(true);
                user.setLockedUntil(Instant.now().plus(LOCK_MINUTES, ChronoUnit.MINUTES));
            }
            users.save(user);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        user.setFailedAttempts(0);
        user.setLocked(false);
        user.setLockedUntil(null);
        users.save(user);
        return issuePair(user);
    }

    @Transactional
    public TokenResponse refresh(String refreshToken) {
        String hash = sha256(refreshToken);
        RefreshToken stored = refreshTokens.findByTokenHash(hash)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh"));
        if (stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh");
        }
        stored.setRevoked(true);
        refreshTokens.save(stored);
        UserAccount user = users.findById(stored.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh"));
        return issuePair(user);
    }

    @Transactional
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        UserAccount user = users.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        users.save(user);
    }

    public MeResponse me(UUID userId) {
        UserAccount user = users.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new MeResponse(user.getId(), user.getEmail(), user.getDisplayName());
    }

    private TokenResponse issuePair(UserAccount user) {
        String access = jwtService.issueAccessToken(user.getId(), user.getEmail());
        byte[] raw = new byte[48];
        random.nextBytes(raw);
        String refresh = Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        RefreshToken entity = new RefreshToken();
        entity.setUserId(user.getId());
        entity.setTokenHash(sha256(refresh));
        entity.setExpiresAt(Instant.now().plus(refreshDays, ChronoUnit.DAYS));
        entity.setRevoked(false);
        refreshTokens.save(entity);
        return new TokenResponse(access, refresh, jwtService.accessTtlSeconds(), user.getId());
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
