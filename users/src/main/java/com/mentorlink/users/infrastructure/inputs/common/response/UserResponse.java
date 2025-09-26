package com.mentorlink.users.infrastructure.inputs.common.response;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String externalId,
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
