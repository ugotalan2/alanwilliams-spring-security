package com.alanwilliams.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClerkPrincipalTest {

    @Test
    void exposesClerkUserId() {
        ClerkPrincipal principal = new ClerkPrincipal("user_123", 42L);

        assertThat(principal.clerkUserId()).isEqualTo("user_123");
        assertThat(principal.platformPersonId()).isEqualTo(42L);
    }

    @Test
    void allowsMissingPlatformPersonId() {
        ClerkPrincipal principal =
                new ClerkPrincipal(
                        "user_123",
                        null
                );

        assertThat(principal.clerkUserId()).isEqualTo("user_123");
        assertThat(principal.platformPersonId()).isNull();
    }
}