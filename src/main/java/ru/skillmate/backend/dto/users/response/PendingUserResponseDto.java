package ru.skillmate.backend.dto.users.response;

public record PendingUserResponseDto(
        String email,
        String message
) {
}
