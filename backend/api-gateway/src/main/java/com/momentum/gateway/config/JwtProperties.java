package com.momentum.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "momentum.jwt")
public class JwtProperties {
    private String secret = "change-me-to-a-256-bit-secret-key-please-use-openssl";

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
