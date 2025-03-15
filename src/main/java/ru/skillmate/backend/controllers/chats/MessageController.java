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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.chats.request.MessageRequest;
import ru.skillmate.backend.dto.chats.response.MessageResponseDto;
import ru.skillmate.backend.dto.errors.ErrorResponseDto;
import ru.skillmate.backend.services.chats.MessageService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "ChatController")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    @Operation(
            summary = "Create a message"
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
    public ResponseEntity<Void> saveMessage(@RequestBody MessageRequest message) {
        messageService.saveMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/upload-media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Create a media message"
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
    public ResponseEntity<Void> uploadFile(@RequestParam String chatId,
                                            @RequestParam MultipartFile file,
                                            Principal principal) {
        messageService.uploadFileMessage(chatId, file, principal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping
    @Operation(
            summary = "Set status 'SEEN' for chat's messages"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Request was accepted successfully"),
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
    public ResponseEntity<Void> setMessagesToSeen(@RequestParam String chatId,
                                                  Principal principal) {
        messageService.setMessagesToSeen(chatId, principal);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/chat/{chatId}")
    @Operation(
            summary = "Get messages by chatId",
            description = "Retrieves all messages of a chat"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requests retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<List<MessageResponseDto>> getMessages(@PathVariable String chatId) {
        return ResponseEntity.ok(messageService.findChatMessages(chatId));
    }

}

