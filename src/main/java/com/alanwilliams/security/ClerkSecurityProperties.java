package com.alanwilliams.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "alanwilliams.security.clerk")
public record ClerkSecurityProperties(

        @NotBlank
        String issuer,

        @NotEmpty
        List<@NotBlank String> authorizedParties

) {
}