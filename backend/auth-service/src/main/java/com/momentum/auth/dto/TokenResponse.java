package com.momentum.auth.dto;

import java.util.UUID;

public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long expiresInSeconds;
    private UUID userId;

    public TokenResponse() {
    }

    public TokenResponse(String accessToken, String refreshToken, long expiresInSeconds, UUID userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresInSeconds = expiresInSeconds;
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }

    public UUID getUserId() {
        return userId;
    }
}
