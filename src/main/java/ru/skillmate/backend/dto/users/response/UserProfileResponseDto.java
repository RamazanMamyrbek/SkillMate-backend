package ru.skillmate.backend.dto.users.response;

import ru.skillmate.backend.entities.users.Gender;

public record UserProfileResponseDto(
        Long id,
        String fullName,
        String email,
        String nickname,
        String country,
        Gender gender,
        String city,
        Long imageResourceId
) {
}
