package com.momentum.auth;

import com.momentum.auth.dto.LoginRequest;
import com.momentum.auth.repository.UserAccountRepository;
import com.momentum.auth.service.AuthService;
import com.momentum.auth.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:auth;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "momentum.jwt.secret=change-me-to-a-256-bit-secret-key-please-use-openssl"
})
class AuthServiceSecurityTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AuthService authService;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserAccountRepository users;

    @BeforeEach
    void unlock() {
        users.findByEmailIgnoreCase("you@momentum.local").ifPresent(user -> {
            user.setLocked(false);
            user.setFailedAttempts(0);
            user.setLockedUntil(null);
            users.save(user);
        });
    }

    @Test
    void loginIssuesJwt() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"you@momentum.local\",\"password\":\"ChangeMeNow!\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void lockoutAfterFiveFailures() {
        LoginRequest bad = new LoginRequest();
        bad.setEmail("you@momentum.local");
        bad.setPassword("wrong");
        for (int i = 0; i < 5; i++) {
            try {
                authService.login(bad);
            } catch (Exception ignored) {
            }
        }
        try {
            authService.login(bad);
            throw new AssertionError("expected lock");
        } catch (Exception ex) {
            assertThat(ex.getMessage().toLowerCase()).containsAnyOf("locked", "423");
        }
    }

    @Test
    void jwtContainsSubject() {
        LoginRequest ok = new LoginRequest();
        ok.setEmail("you@momentum.local");
        ok.setPassword("ChangeMeNow!");
        var tokens = authService.login(ok);
        assertThat(jwtService.parse(tokens.getAccessToken()).getSubject()).isNotBlank();
    }
}
