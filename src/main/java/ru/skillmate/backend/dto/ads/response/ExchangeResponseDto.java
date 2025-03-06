package ru.skillmate.backend.dto.ads.response;

import ru.skillmate.backend.entities.ads.enums.ExchangeStatus;

public record ExchangeResponseDto(
        Long id,
        Long requesterId,
        Long receiverId,
        Long adId,
        ExchangeStatus exchangeStatus,
        String message
) {
}
