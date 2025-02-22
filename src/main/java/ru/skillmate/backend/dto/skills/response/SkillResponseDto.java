package ru.skillmate.backend.dto.skills.response;

import java.util.List;

public record SkillResponseDto(
        Long id,
        String name,
        String description,
        String level,
        Long userId,
        List<Long> achievementIds
) {
}
