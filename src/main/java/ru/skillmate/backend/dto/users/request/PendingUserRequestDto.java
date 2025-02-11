package ru.skillmate.backend.dto.users.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.skillmate.backend.annotations.validation.Trimmed;

public record PendingUserRequestDto(
        @NotBlank(message = "Full name should not be blank")
        @Size(min = 1, message = "Full name should contain at least 1 character")
        @Trimmed
        String fullName,
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email should not be blank")
        @Trimmed
        String email,
        @NotBlank(message = "Password should not be blank")
        @Size(min = 6, message = "Password should contain at least 6 characters")
        @Trimmed
        String password,
        @NotBlank(message = "Country should not be blank")
        @Trimmed
        String country
) {
}
