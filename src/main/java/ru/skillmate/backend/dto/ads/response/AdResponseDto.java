package ru.skillmate.backend.dto.ads.response;

import ru.skillmate.backend.entities.skills.enums.SkillLevel;

public record AdResponseDto(
        Long id,
        String skillName,
        String description,
        String country,
        String city,
        SkillLevel level,
        Long imageResourceId,
        Long userId
) {
}
