package com.alanwilliams.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public class ClerkAuthenticationToken
        extends AbstractAuthenticationToken {

    private final ClerkPrincipal principal;
    private final Jwt jwt;

    public ClerkAuthenticationToken(
            ClerkPrincipal principal,
            Jwt jwt
    ) {
        super(List.of(
                new SimpleGrantedAuthority("ROLE_AUTHENTICATED")
        ));

        this.principal = principal;
        this.jwt = jwt;

        setAuthenticated(true);
    }

    @Override
    public ClerkPrincipal getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    public Jwt getJwt() {
        return jwt;
    }
}