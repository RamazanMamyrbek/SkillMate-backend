package ru.skillmate.backend.dto.users.request;

public record UserLoginRequestDto(
        String email,
        String password
) {
}
