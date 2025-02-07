package ru.skillmate.backend.dto.users.response;

public record UserResponseDto(
        Long id,
        String fullName,

        String email,
        String country
) {
}
