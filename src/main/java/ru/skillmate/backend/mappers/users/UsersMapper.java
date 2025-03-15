package ru.skillmate.backend.mappers.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.skillmate.backend.dto.users.request.PendingUserRequestDto;
import ru.skillmate.backend.dto.users.request.ProfileEditRequestDto;
import ru.skillmate.backend.dto.users.response.PendingUserResponseDto;
import ru.skillmate.backend.dto.users.response.UserProfileResponseDto;
import ru.skillmate.backend.entities.resources.Resource;
import ru.skillmate.backend.entities.users.Gender;
import ru.skillmate.backend.entities.users.PendingUser;
import ru.skillmate.backend.entities.users.Users;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    PendingUser pendingUserRequestDtoToEntity(PendingUserRequestDto pendingUserRequestDto);

    @Mapping(target = "message", constant = "Confirmation code was sent to email")
    PendingUserResponseDto pendingUserToPendingUserResponseDto(PendingUser pendingUser);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nickname", source = "email", qualifiedByName = "generateNickname")
    Users pendingUserToUser(PendingUser pendingUser);

    @Mapping(target = "imageResourceId", source = "user.imageResource", qualifiedByName = "mapResourceToId")
    @Mapping(target = "isOnline", expression = "java(user.isUserOnline())")
    UserProfileResponseDto userToUserResponseDto(Users user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gender", source = "gender", qualifiedByName = "extractGender")
    void updateUserProfile(@MappingTarget Users user, ProfileEditRequestDto profileEditRequestDto);

    @Named("extractGender")
    default Gender extractGender(String genderStr) {
        return Gender.valueOf(genderStr.toUpperCase());
    }

    @Named("generateNickname")
    default String generateNickname(String email) {
        return email.substring(0, email.indexOf("@"));
    }

    @Named("mapResourceToId")
    default Long mapResourceToId(Resource resource) {
        return resource != null ? resource.getId() : null;
    }
}
