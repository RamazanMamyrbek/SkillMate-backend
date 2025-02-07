package ru.skillmate.backend.dto.users.request;

public record ConfirmEmailRequestDto(
        String email,
        String code
) {
}
