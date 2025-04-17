package ru.skillmate.backend.controllers.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.skillmate.backend.dto.errors.ErrorResponseDto;
import ru.skillmate.backend.dto.users.response.UserProfileResponseDto;
import ru.skillmate.backend.services.users.UsersService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Validated
@Tag(name = "UsersController", description = "Endpoints for users management")
public class UsersController {
    private final UsersService usersService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get all users"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resources were get successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<List<UserProfileResponseDto>> getAllUsers() {
        List<UserProfileResponseDto> responseDtoList = usersService.getAllUsers();
        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping(value = "/{userId}/followers")
    @Operation(
            summary = "Get all followers of user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resources were get successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<List<UserProfileResponseDto>> getFollowers(@PathVariable Long userId) {
        List<UserProfileResponseDto> responseDtoList = usersService.getAllFollowers(userId);
        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping(value = "/{userId}/followings")
    @Operation(
            summary = "Get all followings of user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resources were get successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<List<UserProfileResponseDto>> getFollowings(@PathVariable Long userId) {
        List<UserProfileResponseDto> responseDtoList = usersService.getAllFollowings(userId);
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping(value = "/follow/{userId}")
    @Operation(summary = "Follow the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource was created successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "404", description = "Resource was not found", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<Void> followUser(@PathVariable Long userId,
                                           Principal principal) {
        usersService.followUser(principal.getName(), userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/unfollow/{userId}")
    @Operation(summary = "Unfollow the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resource was deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "404", description = "Resource was not found", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<Void> unfollowUser(@PathVariable Long userId,
                                           Principal principal) {
        usersService.unfollowUser(principal.getName(), userId);
        return ResponseEntity.noContent().build();
    }
}
