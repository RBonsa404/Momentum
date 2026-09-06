package com.momentum.auth.dto;

import java.util.UUID;

public class MeResponse {
    private UUID id;
    private String email;
    private String displayName;

    public MeResponse(UUID id, String email, String displayName) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }
}
