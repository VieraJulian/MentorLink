package com.mentorlink.users.infrastructure.inputs.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank @Size(min = 2, max = 50)
        @Pattern(regexp = "^(?![-_.])[a-zA-Z0-9._-]{2,50}(?<![-_.])$",
                 message = "Only letters, numbers, dots, hyphens, and underscores are allowed, no spaces or leading/trailing symbols")
        String username,

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
        @Size(min = 7, max = 50)
        @Email
        String email,

        @NotBlank
        @Size(min = 2, max = 40)
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s'-]+$",
                 message = "Only letters, spaces, hyphens, and apostrophes are allowed")
        String country,

        @NotBlank
        @Size(min = 2, max = 40)
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s'-]+$",
                 message = "Only letters, spaces, hyphens, and apostrophes are allowed")
        String province,

        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&\\-_#]{8,64}$",
                 message = "Password must include at least one letter and one number")
        String password
) {}
