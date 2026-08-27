package com.alanwilliams.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public class ClerkAuthorizedPartyValidator
        implements OAuth2TokenValidator<Jwt> {

    private static final OAuth2Error INVALID_AUTHORIZED_PARTY =
            new OAuth2Error(
                    "invalid_token",
                    "Clerk JWT contains an unauthorized azp claim",
                    null
            );

    private final List<String> authorizedParties;

    public ClerkAuthorizedPartyValidator(
            List<String> authorizedParties
    ) {
        this.authorizedParties =
                authorizedParties == null
                        ? List.of()
                        : List.copyOf(authorizedParties);
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        String authorizedParty = jwt.getClaimAsString("azp");

        if (authorizedParty == null || authorizedParty.isBlank()) {
            return OAuth2TokenValidatorResult.success();
        }

        if (authorizedParties.contains(authorizedParty)) {
            return OAuth2TokenValidatorResult.success();
        }

        return OAuth2TokenValidatorResult.failure(
                INVALID_AUTHORIZED_PARTY
        );
    }
}