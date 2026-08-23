# AlanWilliams Spring Security - Architecture

## Scope

This document defines the reusable Clerk/Spring Security library boundary for AlanWilliams Java APIs.

Cross-app identity ownership is defined in `ALANWILLIAMS_PLATFORM_ARCHITECTURE.md`.

## Architecture Principle

Authentication is shared; authorization remains domain-specific.

The library answers:

```text
Was this request authenticated by a valid Clerk-issued token?
What Clerk user ID authenticated?
```

It does not answer:

```text
Which Platform Person is this?
Which Agenda organization may they access?
Which Budget workspace may they access?
```

## Runtime Model

There is no `alanwilliams-spring-security` container.

It is packaged as a Java dependency consumed by independently deployed services.

```text
alanwilliams-platform backend ----\
                                   +--> alanwilliams-spring-security library
alanwilliams-agenda backend ------/
```

Future Java APIs can consume the same dependency.

## Authentication Flow

```text
HTTP request
-> Authorization: Bearer <Clerk JWT>
-> Spring Security resource server
-> verify JWT signature
-> verify issuer
-> verify expiration/validity
-> extract Clerk user ID
-> create authenticated application principal
-> continue to application authorization
```

Each backend performs this validation locally. No round-trip to a central AlanWilliams auth service is required for normal token validation.

## Suggested Public Abstractions

Exact class names are not locked yet, but the library should stay small and could expose concepts such as:

```text
ClerkSecurityConfiguration
ClerkPrincipal
ClerkJwtAuthenticationConverter
ClerkSecurityProperties
security test helpers
```

Avoid Agenda-specific naming in this repository.

## Configuration

Configuration should be driven by environment/application properties rather than hard-coded domains.

Expected concepts include:

```text
Clerk issuer
JWKS/resource-server configuration
environment-specific allowed issuer/audience rules if required
```

Exact property names should follow the selected Spring Security/Clerk integration after checking current documentation during implementation.

Secrets stay outside Git.

## Principal Contract

The authenticated principal must provide a stable way to obtain Clerk user ID.

Conceptually:

```text
principal.clerkUserId
```

Applications then map that identity to their own needs.

Platform example:

```text
Clerk user ID
-> Platform Person
```

Agenda example:

```text
Clerk user ID
-> Platform Person resolution
-> Agenda membership/permission
```

## Authorization Boundary

The library must not contain application authorization policy.

Do not add concepts such as:

```text
AgendaAdmin
WardCouncilMember
BudgetOwner
ChoresParent
```

Those are consumer-domain responsibilities.

The library may provide generic hooks/utilities only when multiple real consumers need them.

## HTTP Failure Behavior

Shared authentication failures may be normalized when useful:

```text
missing/invalid authentication -> 401
valid authentication but app denies permission -> 403 (app responsibility)
```

Detailed error payload conventions should remain small and avoid leaking token/account information.

## Testing Strategy

The library should include tests for at least:

- valid token authentication
- missing token
- invalid signature
- wrong issuer
- expired token
- malformed token/claims
- Clerk user ID extraction

Reusable test helpers should make consumer API tests easy without forcing them to depend on live Clerk services.

## Dependency / Versioning Direction

The library should use explicit versions/releases once both Platform and Agenda consume it.

Avoid coupling consumers to an unstable `main` branch artifact in production.

The publishing mechanism is not yet locked. Options such as GitHub Packages or another Maven-compatible package mechanism can be chosen when implementation begins.

## Security Boundaries

- Never log bearer tokens.
- Never store Clerk passwords or recovery credentials.
- Keep test and production Clerk environments isolated.
- Validate JWTs according to Clerk/Spring Security requirements rather than trusting frontend claims.
- Backend authorization remains mandatory after authentication succeeds.

## Current Locked Decisions

- This is a reusable library, not a microservice.
- Each Java backend validates Clerk JWTs locally.
- Clerk user ID is the external authenticated identity.
- Platform Person resolution is outside this library.
- App authorization is outside this library.
- The library must remain free of Agenda/Budget domain assumptions.

