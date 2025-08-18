package com.mentorlink.users.infrastructure.outputs.rest.dto;

public record UserResponse(
        String id,
        String username,
        String firstname,
        String lastname,
        String email,
        String country,
        String province,
        String timezone,
        String imageUrl,
        String role
) {}
