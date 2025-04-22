package ru.skillmate.backend.mappers.ads;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skillmate.backend.dto.ads.response.AdResponseDto;
import ru.skillmate.backend.entities.ads.Ad;
import ru.skillmate.backend.entities.resources.Resource;
import ru.skillmate.backend.entities.users.Users;

@Mapper(componentModel = "spring")
public interface AdMapper {

    @Mapping(target = "imageResourceId", source = "imageResource", qualifiedByName = "mapResourceToId")
    @Mapping(target = "userId", source = "user", qualifiedByName = "mapUserToId")
    AdResponseDto mapToResponseDto(Ad ad);

    @Named("mapResourceToId")
    default Long mapResourceToId(Resource resource) {
        return resource != null ? resource.getId() : null;
    }

    @Named("mapUserToId")
    default Long mapUserToId(Users user) {
        return user != null ? user.getId() : null;
    }
}
