package ru.skillmate.backend.services.users.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillmate.backend.dto.users.request.ConfirmEmailRequestDto;
import ru.skillmate.backend.dto.users.request.PendingUserRequestDto;
import ru.skillmate.backend.dto.users.request.UserLoginRequestDto;
import ru.skillmate.backend.dto.users.response.PendingUserResponseDto;
import ru.skillmate.backend.dto.users.response.UserResponseDto;
import ru.skillmate.backend.entities.users.PendingUser;
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.exceptions.InvalidConfirmationCodeException;
import ru.skillmate.backend.exceptions.ResourceAlreadyTakenException;
import ru.skillmate.backend.mappers.users.UsersMapper;
import ru.skillmate.backend.services.mail.EmailService;
import ru.skillmate.backend.services.users.UsersAuthService;
import ru.skillmate.backend.services.users.UsersService;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersAuthServiceImpl implements UsersAuthService {
    private final UsersService usersService;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
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
    public UserResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        return null;
    }

    @Override
    @Transactional
    public UserResponseDto confirmEmail(ConfirmEmailRequestDto requestDto) {
        PendingUser pendingUser = usersService.getPendingUserByEmail(requestDto.email());
        if(!pendingUser.getEmailConfirmationCode().equals(requestDto.code())) {
            throw new InvalidConfirmationCodeException();
        }
        Users user = usersMapper.pendingUserToUser(pendingUser);
        user = usersService.saveUser(user);
        usersService.deletePendingUserById(pendingUser.getId());
        return usersMapper.userToUserResponseDto(user);
    }

    private String generateConfirmationCode() {
        return String.format("%04d", RANDOM.nextInt(10000));
    }

}
