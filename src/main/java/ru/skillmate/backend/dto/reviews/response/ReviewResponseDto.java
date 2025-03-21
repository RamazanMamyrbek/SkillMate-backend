package ru.skillmate.backend.dto.reviews.response;

public record ReviewResponseDto(
        Long id,
        String text,
        Long rating,
        Long reviewerId,
        Long recipientId
) {
}
