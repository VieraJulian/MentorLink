package com.mentorlink.users.infrastructure.inputs.common.response;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
        String status,
        String message,
        T data,
        Object metadata
) {}
