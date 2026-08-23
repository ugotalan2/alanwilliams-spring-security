# AlanWilliams Spring Security - Project Overview

## Purpose

`alanwilliams-spring-security` is a reusable Java library for Clerk authentication integration across AlanWilliams Spring Boot APIs.

It is intentionally a library, not a deployed authentication microservice.

The first consumers are expected to be:

```text
alanwilliams-platform
alanwilliams-agenda
```

Budget can consume the same library when its Java/Spring backend is introduced.

## Repository

Target repository:

```text
ugotalan2/alanwilliams-spring-security
```

## Role in the Platform

Clerk owns authentication.

This library provides the shared Spring-side plumbing needed for each API to validate Clerk-issued JWTs locally.

Conceptual flow:

```text
Browser
-> Clerk session/token
-> Authorization: Bearer <token>
-> Spring Boot API
-> alanwilliams-spring-security
-> validated Clerk principal
-> application-specific authorization
```

## What This Library Owns

- reusable Spring Security configuration for Clerk JWT validation
- issuer/JWKS/resource-server conventions
- extraction of Clerk user ID into a stable authenticated principal abstraction
- common authentication failure handling where it is truly shared
- reusable security test helpers/fixtures
- environment/configuration conventions for Java services

## What This Library Does Not Own

- passwords or credential storage
- Clerk account lifecycle UI
- Platform Person records
- Person merge/reconciliation
- Agenda organizations/memberships
- Agenda meeting permissions
- Budget workspace permissions
- application-specific authorization decisions
- a network/API gateway

## Design Goal

Consumers should be able to adopt Clerk authentication with very little duplicated code while keeping authorization inside their own domain.

The desired separation is:

```text
shared library:
"Is this Clerk token valid and who authenticated?"

application:
"What is this authenticated person allowed to do here?"
```

## Technology Direction

- Java 21-compatible
- Spring Boot 4.1-compatible
- Spring Security resource-server support
- Clerk-issued JWT validation through configured issuer/JWKS
- automated tests for valid, invalid, expired, and malformed authentication cases

Exact dependency versions and public API should be selected during implementation against the current Spring Boot/Clerk documentation.

## Environment Direction

Support environment-specific Clerk configuration for test and production.

Secrets and environment-specific values remain outside Git.

The library should not bake application hostnames or Agenda-specific assumptions into its implementation.

## Near-Term Work

1. Create repository and build skeleton.
2. Define the minimal public API/configuration surface.
3. Implement Clerk JWT resource-server integration.
4. Implement Clerk principal abstraction.
5. Add tests and test helpers.
6. Consume from `alanwilliams-platform`.
7. Consume from `alanwilliams-agenda`.
8. Refine only when a second consumer proves a shared abstraction is genuinely useful.

## Explicitly Deferred

- custom OAuth/OIDC authorization server
- central auth gateway
- Platform Person/database logic
- application roles/permissions
- generic framework abstractions not required by real consumers

