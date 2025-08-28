package com.mentorlink.users.infrastructure.inputs.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
        @Pattern(regexp = "^(?![-_.])[a-zA-Z0-9._-]{2,50}(?<![-_.])$",
                 message = "Only letters, numbers, dots, hyphens, and underscores are allowed, no spaces or leading/trailing symbols")
        String username,

        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 20, message = "First name must be between 2 and 20 characters")
        @Pattern(regexp = "^[\\p{L}]+(?:\\s[\\p{L}]+)*$",
                 message = "Only letters and spaces are allowed")
        String firstname,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters")
        @Pattern(regexp = "^[\\p{L}]+(?:\\s[\\p{L}]+)*$",
                 message = "Only letters and spaces are allowed")
        String lastname,

        @NotBlank(message = "Email is required")
        @Size(min = 7, max = 50)
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Country is required")
        @Size(min = 2, max = 40, message = "Country must be between 2 and 40 characters")
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s'-]+$",
                 message = "Only letters, spaces, hyphens, and apostrophes are allowed")
        String country,

        @NotBlank(message = "Province is required")
        @Size(min = 2, max = 40, message = "Province must be between 2 and 40 characters")
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s'-]+$",
                 message = "Only letters, spaces, hyphens, and apostrophes are allowed")
        String province,

        @NotBlank(message = "Password is required")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&\\-_#]{8,64}$",
                 message = "Password must include at least one letter and one number")
        String password
) {}
