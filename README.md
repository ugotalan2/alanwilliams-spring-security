# AlanWilliams Spring Security

Reusable Spring Security integration for Clerk-authenticated Java services in the AlanWilliams Apps ecosystem.

## Purpose

`alanwilliams-spring-security` prevents each AlanWilliams Java backend from independently rebuilding the same Clerk JWT and Spring Security plumbing.

It is a shared Java library, not a deployed authentication service.

Expected consumers include:

```text
alanwilliams-platform
alanwilliams-agenda
alanwilliams-budget
future Java services
```

## Responsibilities

This library should contain reusable authentication infrastructure such as:

- Clerk JWT validation configuration
- issuer/JWKS integration
- Spring Security resource-server setup helpers
- Clerk user ID extraction
- a common authenticated principal representation
- consistent authentication failure handling where appropriate
- security test utilities

The goal is to make authentication behavior consistent while allowing each application to retain control of its own authorization rules.

## What Does Not Belong Here

This library must not own application-domain authorization or identity data.

Examples that remain outside this repository:

- Platform `Person` records
- organization memberships
- Agenda meeting permissions
- Budget workspace permissions
- app-specific roles
- invitation workflows
- app-specific user settings

The separation is:

```text
Clerk JWT
   |
   v
alanwilliams-spring-security
   |
   v
Authenticated Clerk principal
   |
   +--> Platform resolves canonical Person
   +--> Agenda applies Agenda authorization
   +--> Budget applies Budget authorization
```

## Repository Direction

```text
alanwilliams-spring-security/
├── src/
├── build configuration
├── ALANWILLIAMS_SPRING_SECURITY_OVERVIEW.md
├── ALANWILLIAMS_SPRING_SECURITY_ARCHITECTURE.md
└── README.md
```

The exact Maven/Gradle module and package structure should be finalized during initial implementation rather than introducing unused abstractions prematurely.

## Technology Direction

- Java 21
- Spring Boot 4.1-compatible
- Spring Security resource server
- JWT validation against Clerk issuer/JWKS configuration
- automated unit/integration tests

The library should remain small and focused so applications can upgrade or replace authentication infrastructure without pulling in unrelated platform-domain code.

## Configuration

Clerk environments are isolated between test and production. Consuming applications supply their environment-specific Clerk configuration through external configuration/environment variables.

Secrets must not be committed to this repository.

The library should provide conventions and reusable configuration while leaving actual environment values to the consuming service.

## Usage Direction

A consuming service should be able to add the library as a dependency and configure Clerk authentication without copying security implementation code.

Conceptually:

```text
HTTP Authorization: Bearer <Clerk JWT>
                 |
                 v
       shared JWT validation
                 |
                 v
       authenticated principal
                 |
                 v
       application authorization
```

JWT validation remains local to each backend. Applications do not need to call the Platform service simply to authenticate every request.

## Publishing / Distribution

The exact artifact publishing mechanism should be selected when the first two repositories consume the library. The important architectural requirement is that consumers use a versioned dependency rather than copying source files between repositories.

Possible future distribution approaches include a private package registry or another controlled Maven-compatible artifact source.

## Documentation

See:

- `ALANWILLIAMS_SPRING_SECURITY_OVERVIEW.md` for scope, current status, and implementation priorities.
- `ALANWILLIAMS_SPRING_SECURITY_ARCHITECTURE.md` for the authentication boundary, principal model, configuration rules, and integration contracts.

Cross-repository identity ownership and Platform Person behavior are defined by `alanwilliams-platform`, not by this library.

## Current Priority

After the existing Agenda naming/deployment conventions are standardized, implement the minimum reusable Clerk/Spring Security foundation required by both Platform and Agenda. Keep the first version intentionally narrow and expand it only when additional consumers demonstrate a shared requirement.
