package com.alanwilliams.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@AutoConfiguration
@EnableConfigurationProperties(ClerkSecurityProperties.class)
public class ClerkSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtDecoder clerkJwtDecoder(
            ClerkSecurityProperties properties
    ) {
        NimbusJwtDecoder decoder =
                NimbusJwtDecoder
                        .withIssuerLocation(properties.issuer())
                        .build();

        OAuth2TokenValidator<Jwt> standardValidators =
                JwtValidators.createDefaultWithIssuer(
                        properties.issuer()
                );

        OAuth2TokenValidator<Jwt> authorizedPartyValidator =
                new ClerkAuthorizedPartyValidator(
                        properties.authorizedParties()
                );

        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(
                        standardValidators,
                        authorizedPartyValidator
                )
        );

        return decoder;
    }

    @Bean
    @ConditionalOnMissingBean
    public ClerkJwtAuthenticationConverter
    clerkJwtAuthenticationConverter() {
        return new ClerkJwtAuthenticationConverter();
    }
}