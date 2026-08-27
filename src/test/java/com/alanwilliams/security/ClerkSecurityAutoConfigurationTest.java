package com.alanwilliams.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ClerkSecurityAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(
                            AutoConfigurations.of(
                                    ClerkSecurityAutoConfiguration.class
                            )
                    )
                    .withPropertyValues(
                            "alanwilliams.security.clerk.issuer=https://example.clerk.accounts.dev",
                            "alanwilliams.security.clerk.authorized-parties[0]=http://localhost:5173"
                    )
                    .withBean(
                            JwtDecoder.class,
                            () -> mock(JwtDecoder.class)
                    );

    @Test
    void createsClerkSecurityBeans() {
        contextRunner.run(context -> {
            assertThat(context)
                    .hasSingleBean(ClerkSecurityProperties.class);

            assertThat(context)
                    .hasSingleBean(JwtDecoder.class);

            assertThat(context)
                    .hasSingleBean(
                            ClerkJwtAuthenticationConverter.class
                    );
        });
    }

    @Test
    void failsWhenClerkConfigurationIsMissing() {
        new ApplicationContextRunner()
                .withConfiguration(
                        AutoConfigurations.of(
                                ClerkSecurityAutoConfiguration.class
                        )
                )
                .withBean(
                        JwtDecoder.class,
                        () -> mock(JwtDecoder.class)
                )
                .run(context -> {
                    assertThat(context)
                            .hasFailed();

                    assertThat(context.getStartupFailure())
                            .hasMessageContaining(
                                    "ClerkSecurityProperties"
                            );
                });
    }
}