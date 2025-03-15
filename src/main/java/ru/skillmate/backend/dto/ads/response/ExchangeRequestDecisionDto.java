package ru.skillmate.backend.dto.ads.response;


public record ExchangeRequestDecisionDto(
        String chatId,
        ExchangeResponseDto exchangeResponseDto
) {
}
