package ru.skillmate.backend.mappers.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillmate.backend.dto.users.request.PendingUserRequestDto;
import ru.skillmate.backend.dto.users.response.PendingUserResponseDto;
import ru.skillmate.backend.dto.users.response.UserResponseDto;
import ru.skillmate.backend.entities.users.PendingUser;
import ru.skillmate.backend.entities.users.Users;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    PendingUser pendingUserRequestDtoToEntity(PendingUserRequestDto pendingUserRequestDto);

    @Mapping(target = "message", constant = "Confirmation code was sent to email")
    PendingUserResponseDto pendingUserToPendingUserResponseDto(PendingUser pendingUser);

    @Mapping(target = "id", ignore = true)
    Users pendingUserToUser(PendingUser pendingUser);

    UserResponseDto userToUserResponseDto(Users user);
}
