package ru.skillmate.backend.mappers.ads;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skillmate.backend.dto.ads.response.ExchangeResponseDto;
import ru.skillmate.backend.entities.ads.Ad;
import ru.skillmate.backend.entities.ads.ExchangeRequest;
import ru.skillmate.backend.entities.users.Users;

@Mapper(componentModel = "spring")
public interface ExchangeRequestMapper {

    @Mapping(target = "requesterId", source = "requester", qualifiedByName = "mapUserToId")
    @Mapping(target = "receiverId", source = "receiver", qualifiedByName = "mapUserToId")
    @Mapping(target = "adId", source = "ad", qualifiedByName = "mapAdToId")
    ExchangeResponseDto mapToResponseDto(ExchangeRequest exchangeRequest);

    @Named("mapUserToId")
    default Long mapUserToId(Users user) {
        return user.getId();
    }

    @Named("mapAdToId")
    default Long mapAdToId(Ad ad) {
        return ad.getId();
    }
}
