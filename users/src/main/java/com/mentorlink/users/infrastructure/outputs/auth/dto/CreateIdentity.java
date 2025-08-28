package com.mentorlink.users.infrastructure.outputs.auth.dto;

public record CreateIdentity(
        String id,
        String username,
        String firstname,
        String lastname,
        String email,
        String password,
        String role
){}
