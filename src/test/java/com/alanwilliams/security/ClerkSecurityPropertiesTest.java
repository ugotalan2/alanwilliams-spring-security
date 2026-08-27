package com.alanwilliams.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ClerkSecurityPropertiesTest {

    @Test
    void bindsClerkSecurityProperties() {
        var source = new MapConfigurationPropertySource(
                Map.of(
                        "alanwilliams.security.clerk.issuer",
                        "https://example.clerk.accounts.dev",
                        "alanwilliams.security.clerk.authorized-parties[0]",
                        "http://localhost:5173"
                )
        );

        ClerkSecurityProperties properties =
                new Binder(source)
                        .bind(
                                "alanwilliams.security.clerk",
                                ClerkSecurityProperties.class
                        )
                        .orElseThrow(() -> new IllegalStateException("Failed to bind Clerk security properties"));

        assertThat(properties.issuer())
                .isEqualTo("https://example.clerk.accounts.dev");

        assertThat(properties.authorizedParties())
                .containsExactly("http://localhost:5173");
    }
}