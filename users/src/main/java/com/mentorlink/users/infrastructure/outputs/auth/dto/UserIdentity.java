package com.mentorlink.users.infrastructure.outputs.auth.dto;

public record UserIdentity(
        String id,
        String username,
        String firstname,
        String lastname,
        String email,
        String role
)
{}
