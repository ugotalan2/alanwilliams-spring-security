package com.alanwilliams.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClerkJwtAuthenticationConverterTest {

    private final ClerkJwtAuthenticationConverter converter =
            new ClerkJwtAuthenticationConverter();

    @Test
    void convertsJwtSubjectToClerkPrincipal() {
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .subject("user_123")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();

        ClerkAuthenticationToken authentication =
                (ClerkAuthenticationToken) converter.convert(jwt);

        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getPrincipal().clerkUserId())
                .isEqualTo("user_123");
        assertThat(authentication.getJwt()).isSameAs(jwt);
    }

    @Test
    void rejectsJwtWithoutSubject() {
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();

        assertThatThrownBy(() -> converter.convert(jwt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Clerk JWT is missing the subject claim");
    }

    @Test
    void rejectsJwtWithBlankSubject() {
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .claim("sub", " ")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();

        assertThatThrownBy(() -> converter.convert(jwt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Clerk JWT is missing the subject claim");
    }
}