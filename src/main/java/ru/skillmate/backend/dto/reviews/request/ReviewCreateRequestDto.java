package ru.skillmate.backend.dto.reviews.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReviewCreateRequestDto(
        @Min(value = 1, message = "recipientId should be greater than 0")
        Long recipientId,
        String text,
        @Min(value = 1, message = "Should be min 0")
        @Max(value = 5, message = "Should be max 5")
        Long rating
) {
}
