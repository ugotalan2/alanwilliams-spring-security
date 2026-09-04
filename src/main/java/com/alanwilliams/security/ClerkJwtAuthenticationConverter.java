package com.alanwilliams.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

public class ClerkJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String clerkUserId = jwt.getSubject();

        Number platformPersonIdClaim =
                jwt.getClaim("platform_person_id");

        Long platformPersonId =
                platformPersonIdClaim == null
                        ? null
                        : platformPersonIdClaim.longValue();

        if (clerkUserId == null || clerkUserId.isBlank()) {
            throw new IllegalArgumentException(
                    "Clerk JWT is missing the subject claim"
            );
        }

        return new ClerkAuthenticationToken(
                new ClerkPrincipal(
                        clerkUserId,
                        platformPersonId
                ),
                jwt
        );
    }
}