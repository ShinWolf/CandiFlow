package com.example.candiflow;

import com.example.candiflow.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "test-secret-key-very-long-32chars!!");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);
    }

    @Test
    void shouldGenerateToken() {
        String token = jwtService.generateToken("test@test.com");
        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void shouldExtractEmail() {
        String token = jwtService.generateToken("test@test.com");
        assertThat(jwtService.extractEmail(token)).isEqualTo("test@test.com");
    }

    @Test
    void shouldValidateToken() {
        String token = jwtService.generateToken("test@test.com");
        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    void shouldRejectInvalidToken() {
        assertThat(jwtService.isTokenValid("token.invalide.ici")).isFalse();
    }
}
