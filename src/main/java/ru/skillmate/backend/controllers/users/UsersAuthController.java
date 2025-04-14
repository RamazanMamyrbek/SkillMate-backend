package ru.skillmate.backend.controllers.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.skillmate.backend.annotations.validation.Trimmed;
import ru.skillmate.backend.dto.common.StringResponse;
import ru.skillmate.backend.dto.users.request.ConfirmEmailRequestDto;
import ru.skillmate.backend.dto.users.request.PendingUserRequestDto;
import ru.skillmate.backend.dto.users.request.UserLoginRequestDto;
import ru.skillmate.backend.dto.users.response.PendingUserResponseDto;
import ru.skillmate.backend.dto.users.response.UserProfileResponseDto;
import ru.skillmate.backend.services.users.UsersAuthService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/auth")
@Validated
@Tag(name = "UsersAuthController", description = "Endpoints for authentication")
public class UsersAuthController {
    private final UsersAuthService userAuthService;

    @PostMapping("/register")
    @Operation(summary = "Endpoint to make a register request")
    public ResponseEntity<PendingUserResponseDto> register(@RequestBody @Valid PendingUserRequestDto registerRequestDto) {
        PendingUserResponseDto pendingUserResponseDto = userAuthService.registerRequest(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pendingUserResponseDto);
    }

    @PostMapping("/confirm-email")
    @Operation(summary = "Endpoint to confirm email via code")
    public ResponseEntity<UserProfileResponseDto> confirmEmail(@RequestBody @Valid ConfirmEmailRequestDto requestDto) {
        UserProfileResponseDto responseDto = userAuthService.confirmEmail(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/resend-code")
    @Operation(summary = "Endpoint to resend confirmation code")
    public ResponseEntity<Map<String, String>> resendCode(@RequestParam @Trimmed @Email(message = "Invalid email format") String email) {
        userAuthService.resendCode(email);
        return ResponseEntity.ok().body(Map.of("message", "Confirmation code was sent"));
    }

    @PostMapping("/login")
    @Operation(summary = "Endpoint to make a login request")
    public ResponseEntity<UserProfileResponseDto> login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        UserProfileResponseDto userProfileResponseDto = userAuthService.login(userLoginRequestDto, response);
        return ResponseEntity.status(HttpStatus.OK).body(userProfileResponseDto);
    }

    @GetMapping("/logout")
    @Operation(summary = "Endpoint to logout by annulling tokens")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        userAuthService.logout(response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Endpoint for forgot password")
    public ResponseEntity<StringResponse> forgotPassword(@RequestParam @Email(message = "Invalid email format") String email) {
        userAuthService.sendResetPasswordLink(email);
        return ResponseEntity.ok(new StringResponse("Reset password link was sent"));
    }

    @PatchMapping("/reset-password")
    @Operation(summary = "Endpoint for changing password by token")
    public ResponseEntity<StringResponse> resetPassword(@RequestParam String token,
                                                 @RequestParam @NotBlank(message = "Password should not be blank")
                                                 @Size(min = 6, message = "Password should contain at least 6 characters")
                                                 @Trimmed String newPassword) {
        userAuthService.resetPasswordByToken(token, newPassword);
        return ResponseEntity.ok(new StringResponse("Password was successfully changed"));
    }
}
