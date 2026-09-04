package com.alanwilliams.security;

public record ClerkPrincipal(
        String clerkUserId,
        Long platformPersonId
) {
}
