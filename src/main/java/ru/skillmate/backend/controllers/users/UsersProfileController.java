package ru.skillmate.backend.controllers.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.users.request.ProfileEditRequestDto;
import ru.skillmate.backend.dto.users.response.UserProfileResponseDto;
import ru.skillmate.backend.services.users.UsersService;

import java.security.Principal;

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
            @ApiResponse(responseCode = "403", description = "Authorization error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserProfileResponseDto> getProfileInfo(Principal principal) {
        UserProfileResponseDto responseDto = usersService.getProfileInfo(principal.getName());
        return ResponseEntity.ok().body(responseDto);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Edit profile"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile was edited successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Authorization error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserProfileResponseDto> editProfile(Principal principal,
                                                              @RequestBody @Valid ProfileEditRequestDto profileEditRequestDto) {
        UserProfileResponseDto responseDto = usersService.editProfile(principal.getName(), profileEditRequestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Edit profile image"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile image was edited successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Authorization error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserProfileResponseDto> editProfile(Principal principal,
                                                              @RequestParam MultipartFile image) {
        UserProfileResponseDto responseDto = usersService.editProfileImage(principal.getName(), image);
        return ResponseEntity.ok().body(responseDto);
    }


}
