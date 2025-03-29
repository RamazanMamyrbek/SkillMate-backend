package ru.skillmate.backend.dto.posts.response;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDto(
        Long id,
        List<String> categories,
        String text,
        LocalDateTime createdAt,
        Long resourceId,
        Long creatorId,
        String creatorNickname,
        String creatorFullName
) {
}
