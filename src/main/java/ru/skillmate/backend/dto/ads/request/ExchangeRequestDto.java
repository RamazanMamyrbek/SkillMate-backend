package ru.skillmate.backend.dto.ads.request;

import jakarta.validation.constraints.Min;
import ru.skillmate.backend.entities.ads.enums.ExchangeStatus;

public record ExchangeRequestDto(
        @Min(value = 1, message = "adId should be greater than 0")
        Long adId,
        String message
) {
}
