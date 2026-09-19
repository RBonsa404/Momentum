package com.momentum.server.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

/**
 * Utilitaire pour extraire le userId courant depuis le SecurityContext.
 * Remplace l'extraction depuis le header X-User-Id (qui était injecté par le gateway).
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    public static UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authentication");
        }
        try {
            return UUID.fromString((String) auth.getPrincipal());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user identity");
        }
    }

    public static UUID getUserId() {
        return currentUserId();
    }
}
