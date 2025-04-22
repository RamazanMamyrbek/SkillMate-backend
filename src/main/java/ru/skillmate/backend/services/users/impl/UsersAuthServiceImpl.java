package ru.skillmate.backend.services.users.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillmate.backend.configurations.security.JwtProvider;
import ru.skillmate.backend.dto.users.request.ConfirmEmailRequestDto;
import ru.skillmate.backend.dto.users.request.PendingUserRequestDto;
import ru.skillmate.backend.dto.users.request.UserLoginRequestDto;
import ru.skillmate.backend.dto.users.response.PendingUserResponseDto;
import ru.skillmate.backend.dto.users.response.UserProfileResponseDto;
import ru.skillmate.backend.entities.users.PendingUser;
import ru.skillmate.backend.entities.users.ResetPasswordToken;
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.exceptions.*;
import ru.skillmate.backend.mappers.users.UsersMapper;
import ru.skillmate.backend.repositories.users.ResetPasswordTokenRepository;
import ru.skillmate.backend.services.mail.EmailService;
import ru.skillmate.backend.services.users.UsersAuthService;
import ru.skillmate.backend.services.users.UsersService;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersAuthServiceImpl implements UsersAuthService {
    private final UsersService usersService;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    @Value("${application.frontend.url}")
    private String applicationFrontendUrl;
    private static final Random RANDOM = new Random();

    @Override
    @Transactional
    public PendingUserResponseDto registerRequest(PendingUserRequestDto registerRequestDto) {
        if(usersService.isEmailUnique(registerRequestDto.email())) {
            throw ResourceAlreadyTakenException.userEmailWasAlreadyTaken(registerRequestDto.email());
        }
        PendingUser pendingUser = usersMapper.pendingUserRequestDtoToEntity(registerRequestDto);
        pendingUser.setPasswordHash(passwordEncoder.encode(registerRequestDto.password()));
        String code = generateConfirmationCode();
        pendingUser.setEmailConfirmationCode(code);
        emailService.sendConfirmationCode(registerRequestDto.email(), code);
        if(usersService.isPendingEmailUnique(registerRequestDto.email())) {
            pendingUser = usersService.getPendingUserByEmail(registerRequestDto.email());
            pendingUser.setEmailConfirmationCode(code);
        } else {
            pendingUser = usersService.savePendingUser(pendingUser);
        }
        return usersMapper.pendingUserToPendingUserResponseDto(pendingUser);
    }

    @Override
    @Transactional
    public UserProfileResponseDto login(UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequestDto.email(), userLoginRequestDto.password()));
        } catch (Exception ex) {
            throw new LoginFailedException(ex.getMessage());
        }
        String accessToken = jwtProvider.generateAccessToken(userLoginRequestDto.email());
        String refreshToken = jwtProvider.generateRefreshToken(userLoginRequestDto.email());
        addCookies(response, "accessToken", accessToken, jwtProvider.getAccessKeyExpiration());
        addCookies(response, "refreshToken", refreshToken, jwtProvider.getRefreshKeyExpiration());
        Users user = (Users) usersService.getUserByUsername(userLoginRequestDto.email());
        return usersMapper.userToUserResponseDto(user);
    }

    @Override
    @Transactional
    public UserProfileResponseDto confirmEmail(ConfirmEmailRequestDto requestDto) {
        PendingUser pendingUser = usersService.getPendingUserByEmail(requestDto.email());
        if(!pendingUser.getEmailConfirmationCode().equals(requestDto.code())) {
            throw new InvalidConfirmationCodeException();
        }
        Users user = usersMapper.pendingUserToUser(pendingUser);
        user = usersService.saveUser(user);
        usersService.deletePendingUserById(pendingUser.getId());
        return usersMapper.userToUserResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public void logout(HttpServletResponse response) {
        addCookies(response, "accessToken", null, 0);
        addCookies(response, "refreshToken", null, 0);
    }

    @Override
    @Transactional
    public void resendCode(String email) {
        PendingUser pendingUser = usersService.getPendingUserByEmail(email);
        String code = generateConfirmationCode();
        emailService.sendConfirmationCode(email, code);
        pendingUser.setEmailConfirmationCode(code);
        usersService.savePendingUser(pendingUser);
    }

    @Override
    @Transactional
    public void sendResetPasswordLink(String email) {
        Users user = usersService.getUserByEmail(email);
        String tokenStr = UUID.randomUUID().toString();
        String link = applicationFrontendUrl + "/reset-password?token=" + tokenStr;
        ResetPasswordToken token;
        if(resetPasswordTokenRepository.existsByUser(user)) {
            token = resetPasswordTokenRepository.findByUser(user).get();
        } else {
            token = ResetPasswordToken
                    .builder()
                    .user(user)
                    .token(tokenStr)
                    .link(link)
                    .build();
            resetPasswordTokenRepository.save(token);
        }
        resetPasswordTokenRepository.save(token);
        emailService.sendResetPasswordLink(email, token);
    }

    @Override
    public void resetPasswordByToken(String token, String newPassword) {
        ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByToken(token).orElseThrow(
                () -> ResourceNotFoundException.resetPasswordTokenNotFound(token)
        );
        if(resetPasswordToken.getCreatedAt().plusMinutes(15).isBefore(LocalDateTime.now())) {
            resetPasswordTokenRepository.deleteById(resetPasswordToken.getId());
            resetPasswordTokenRepository.flush();
            throw ResourceExpiredException.resetPasswordTokenExpired(token);
        }
        Users user = resetPasswordToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        usersService.saveUser(user);
        resetPasswordTokenRepository.delete(resetPasswordToken);
    }

    private String generateConfirmationCode() {
        return String.format("%04d", RANDOM.nextInt(10000));
    }


    private void addCookies(HttpServletResponse response, String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

}
