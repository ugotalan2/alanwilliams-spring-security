package com.alanwilliams.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClerkAuthorizedPartyValidatorTest {

    private final ClerkAuthorizedPartyValidator validator =
            new ClerkAuthorizedPartyValidator(
                    List.of(
                            "http://localhost:5173",
                            "https://test.alanwilliams.app"
                    )
            );

    @Test
    void acceptsAuthorizedParty() {
        Jwt jwt = jwtWithAuthorizedParty(
                "http://localhost:5173"
        );

        var result = validator.validate(jwt);

        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    void rejectsUnauthorizedParty() {
        Jwt jwt = jwtWithAuthorizedParty(
                "https://evil.example.com"
        );

        var result = validator.validate(jwt);

        assertThat(result.hasErrors()).isTrue();
    }

    @Test
    void acceptsMissingAuthorizedParty() {
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .subject("user_123")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();

        var result = validator.validate(jwt);

        assertThat(result.hasErrors()).isFalse();
    }

    private Jwt jwtWithAuthorizedParty(String authorizedParty) {
        return Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .subject("user_123")
                .claim("azp", authorizedParty)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();
    }
}