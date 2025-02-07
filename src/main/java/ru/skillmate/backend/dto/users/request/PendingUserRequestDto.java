package ru.skillmate.backend.dto.users.request;

public record PendingUserRequestDto(
        String fullName,

        String email,
        String password,
        String country
) {
}
