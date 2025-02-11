package ru.skillmate.backend.services.users;

import jakarta.servlet.http.HttpServletResponse;
import ru.skillmate.backend.dto.users.request.ConfirmEmailRequestDto;
import ru.skillmate.backend.dto.users.request.PendingUserRequestDto;
import ru.skillmate.backend.dto.users.request.UserLoginRequestDto;
import ru.skillmate.backend.dto.users.response.PendingUserResponseDto;
import ru.skillmate.backend.dto.users.response.UserResponseDto;

public interface UsersAuthService {
    PendingUserResponseDto registerRequest(PendingUserRequestDto registerRequestDto);

    UserResponseDto login(UserLoginRequestDto userLoginRequestDto, HttpServletResponse response);

    UserResponseDto confirmEmail(ConfirmEmailRequestDto requestDto);

    void logout(HttpServletResponse response);

    void resendCode(String email);
}

