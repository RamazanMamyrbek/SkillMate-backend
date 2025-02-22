package ru.skillmate.backend.dto.users.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.skillmate.backend.annotations.validation.EnumPattern;
import ru.skillmate.backend.annotations.validation.Trimmed;
import ru.skillmate.backend.entities.users.Gender;

public record ProfileEditRequestDto(
        @NotBlank(message = "Full name should not be blank")
        @Size(min = 1, message = "Full name should contain at least 1 character")
        @Trimmed
        String fullName,
        @NotBlank(message = "Email should not be blank")
        @Trimmed
        String nickname,
        @NotBlank(message = "Country should not be blank")
        @Trimmed
        String country,
        @NotBlank(message = "Country should not be blank")
        @Trimmed
        String city,
        @NotBlank(message = "Country should not be blank")
        @Trimmed
        @EnumPattern(enumClass = Gender.class, message = "Valid values for gender: MALE|FEMALE|UNDEFINED")
        @Schema(description = "Valid values: MALE|FEMALE|UNDEFINED")
        String gender
) {
}
