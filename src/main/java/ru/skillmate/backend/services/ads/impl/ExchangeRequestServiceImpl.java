package ru.skillmate.backend.services.ads.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillmate.backend.dto.ads.request.ExchangeRequestDto;
import ru.skillmate.backend.dto.ads.response.ExchangeResponseDto;
import ru.skillmate.backend.entities.ads.Ad;
import ru.skillmate.backend.entities.ads.ExchangeRequest;
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.exceptions.IllegalArgumentException;
import ru.skillmate.backend.exceptions.ResourceAlreadyTakenException;
import ru.skillmate.backend.mappers.ads.ExchangeRequestMapper;
import ru.skillmate.backend.repositories.ads.ExchangeRequestRepository;
import ru.skillmate.backend.services.ads.AdService;
import ru.skillmate.backend.services.ads.ExchangeRequestService;
import ru.skillmate.backend.services.mail.EmailService;
import ru.skillmate.backend.services.users.UsersService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExchangeRequestServiceImpl implements ExchangeRequestService {
    private final ExchangeRequestRepository exchangeRequestRepository;
    private final UsersService usersService;
    private final AdService adService;
    private final ExchangeRequestMapper exchangeRequestMapper;
    private final EmailService emailService;
    @Override
    @Transactional
    public ExchangeResponseDto createExchangeRequest(ExchangeRequestDto exchangeRequestDto, String requesterEmail) {
        Users requester = (Users) usersService.getUserByUsername(requesterEmail);
        Ad ad  = adService.getAdById(exchangeRequestDto.adId());
        checkByRequesterAndAd(requester, ad);
        if(requester.getId().equals(ad.getUser().getId())) {
            throw IllegalArgumentException.userCannotRequestOwnAd(requester.getId(), ad.getId());
        }
        ExchangeRequest exchangeRequest = ExchangeRequest
                .builder()
                .requester(requester)
                .receiver(ad.getUser())
                .ad(ad)
                .message(exchangeRequestDto.message())
                .build();
        exchangeRequest = exchangeRequestRepository.save(exchangeRequest);
        emailService.sendExchangeRequestNotification(requester, ad, exchangeRequest);
        return exchangeRequestMapper.mapToResponseDto(exchangeRequest);
    }

    @Override
    public List<ExchangeResponseDto> getSentExchangeRequests(String email) {
        Users requester = (Users) usersService.getUserByUsername(email);
        return exchangeRequestRepository
                .findAllByRequester(requester)
                .stream()
                .map(exchangeRequestMapper::mapToResponseDto)
                .toList();
    }

    @Override
    public List<ExchangeResponseDto> getReceivedExchangeRequests(String email) {
        Users receiver = (Users) usersService.getUserByUsername(email);
        return exchangeRequestRepository
                .findAllByReceiver(receiver)
                .stream()
                .map(exchangeRequestMapper::mapToResponseDto)
                .toList();
    }

    private void checkByRequesterAndAd(Users requester, Ad ad) {
        if(exchangeRequestRepository.existsByRequesterAndAd(requester, ad)) {
            throw ResourceAlreadyTakenException.exchangeRequestAlreadyExistsByRequesterAndAd(requester.getId(), ad.getId());
        }
    }
}
