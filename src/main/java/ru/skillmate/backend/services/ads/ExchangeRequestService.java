package ru.skillmate.backend.services.ads;

import ru.skillmate.backend.dto.ads.request.ExchangeRequestDto;
import ru.skillmate.backend.dto.ads.response.ExchangeRequestDecisionDto;
import ru.skillmate.backend.dto.ads.response.ExchangeResponseDto;
import ru.skillmate.backend.entities.ads.enums.ExchangeStatus;

import java.util.List;

public interface ExchangeRequestService {
    ExchangeResponseDto createExchangeRequest(ExchangeRequestDto exchangeRequestDto, String requesterEmail);

    List<ExchangeResponseDto> getSentExchangeRequests(String email);

    List<ExchangeResponseDto> getReceivedExchangeRequests(String email);

    ExchangeRequestDecisionDto acceptOrDecline(Long requestId, ExchangeStatus status, String email);
}
