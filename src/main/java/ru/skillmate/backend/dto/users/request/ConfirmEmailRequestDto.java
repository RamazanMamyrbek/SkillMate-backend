package ru.skillmate.backend.dto.users.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import ru.skillmate.backend.annotations.validation.Trimmed;

public record ConfirmEmailRequestDto(
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email should not be blank")
        @Trimmed
        String email,
        @NotBlank(message = "Confirmation code should not be blank")
        @Trimmed
        String code
) {
}
