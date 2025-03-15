package ru.skillmate.backend.controllers.chats;

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
import ru.skillmate.backend.dto.chats.response.ChatResponse;
import ru.skillmate.backend.dto.common.StringResponse;
import ru.skillmate.backend.dto.errors.ErrorResponseDto;
import ru.skillmate.backend.dto.users.response.UserProfileResponseDto;
import ru.skillmate.backend.services.chats.ChatService;
import ru.skillmate.backend.services.users.UsersService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Validated
@Tag(name = "ChatController", description = "Endpoints for chats management")
public class ChatController {
    private final ChatService chatService;
    private final UsersService usersService;

    @PostMapping
    @Operation(
            summary = "Start a chat"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resource was created successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<StringResponse> createChat(
            @RequestParam Long senderId,
            @RequestParam Long receiverId
    ) {
        String chatId = chatService.createChat(senderId, receiverId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new StringResponse(chatId));
    }

    @GetMapping
    @Operation(
            summary = "Get my chats",
            description = "Retrieves all chats of current user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requests retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<List<ChatResponse>> getChatsByReceiver(Principal principal) {
        return ResponseEntity.ok(chatService.getChatsByReceiverId(principal));
    }

}
