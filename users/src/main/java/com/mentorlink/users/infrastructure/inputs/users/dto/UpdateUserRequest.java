package com.mentorlink.users.infrastructure.inputs.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank
        @Size(min = 2, max = 20)
        @Pattern(regexp = "^[\\p{L}]+(?:\\s[\\p{L}]+)*$",
                message = "Only letters and spaces are allowed")
        String firstname,

        @NotBlank
        @Size(min = 2, max = 20)
        @Pattern(regexp = "^[\\p{L}]+(?:\\s[\\p{L}]+)*$",
                message = "Only letters and spaces are allowed")
        String lastname,

        @NotBlank
        @Size(min = 2, max = 40)
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s'-]+$",
                message = "Only letters, spaces, hyphens, and apostrophes are allowed")
        String country,

        @NotBlank
        @Size(min = 2, max = 40)
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s'-]+$",
                message = "Only letters, spaces, hyphens, and apostrophes are allowed")
        String province
) {}
