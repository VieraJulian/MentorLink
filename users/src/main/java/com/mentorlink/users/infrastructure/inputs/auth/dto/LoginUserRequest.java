package com.mentorlink.users.infrastructure.inputs.auth.dto;

public record LoginUserRequest(
        String username,
        String password
) {
}
