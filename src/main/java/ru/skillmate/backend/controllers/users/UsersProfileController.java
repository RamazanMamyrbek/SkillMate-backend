package ru.skillmate.backend.controllers.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.annotations.validation.Trimmed;
import ru.skillmate.backend.dto.common.StringResponse;
import ru.skillmate.backend.dto.errors.ErrorResponseDto;
import ru.skillmate.backend.dto.users.request.ProfileEditRequestDto;
import ru.skillmate.backend.dto.users.response.UserProfileResponseDto;
import ru.skillmate.backend.services.users.UsersService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/profile")
@Validated
@Tag(name = "UsersProfileController", description = "Endpoints for profile management")
public class UsersProfileController {
    private final UsersService usersService;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get profile info"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile was get successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<UserProfileResponseDto> getProfileInfo(Principal principal) {
        UserProfileResponseDto responseDto = usersService.getProfileInfo(principal.getName());
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get user info"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info was get successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "404", description = "User was not found", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<UserProfileResponseDto> getProfileInfo(@PathVariable Long userId) {
        UserProfileResponseDto responseDto = usersService.getUserInfo(userId);
        return ResponseEntity.ok().body(responseDto);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Edit profile"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile was edited successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<UserProfileResponseDto> editProfile(Principal principal,
                                                              @RequestBody @Valid ProfileEditRequestDto profileEditRequestDto) {
        UserProfileResponseDto responseDto = usersService.editProfile(principal.getName(), profileEditRequestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Edit profile image"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile image was edited successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<UserProfileResponseDto> editProfile(Principal principal,
                                                              @RequestParam MultipartFile image) {
        UserProfileResponseDto responseDto = usersService.editProfileImage(principal.getName(), image);
        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/change-password")
    @Operation(
            summary = "Change password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource was updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<StringResponse> changePassword(Principal principal,
                                                         @RequestParam @NotBlank(message = "Password should not be blank")
                                                         @Size(min = 6, message = "Password should contain at least 6 characters")
                                                         @Trimmed String oldPassword,
                                                         @RequestParam @NotBlank(message = "Password should not be blank")
                                                              @Size(min = 6, message = "Password should contain at least 6 characters")
                                                              @Trimmed String newPassword) {
        usersService.changePassword(principal.getName(), newPassword, oldPassword);
        return ResponseEntity.ok(new StringResponse("Password was changed successfully"));
    }


}
