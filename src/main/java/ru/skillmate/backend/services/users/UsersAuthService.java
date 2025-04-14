package ru.skillmate.backend.services.users;

import jakarta.servlet.http.HttpServletResponse;
import ru.skillmate.backend.dto.users.request.ConfirmEmailRequestDto;
import ru.skillmate.backend.dto.users.request.PendingUserRequestDto;
import ru.skillmate.backend.dto.users.request.UserLoginRequestDto;
import ru.skillmate.backend.dto.users.response.PendingUserResponseDto;
import ru.skillmate.backend.dto.users.response.UserProfileResponseDto;

public interface UsersAuthService {
    PendingUserResponseDto registerRequest(PendingUserRequestDto registerRequestDto);

    UserProfileResponseDto login(UserLoginRequestDto userLoginRequestDto, HttpServletResponse response);

    UserProfileResponseDto confirmEmail(ConfirmEmailRequestDto requestDto);

    void logout(HttpServletResponse response);

    void resendCode(String email);

    void sendResetPasswordLink(String email);

    void resetPasswordByToken(String token, String newPassword);
}

