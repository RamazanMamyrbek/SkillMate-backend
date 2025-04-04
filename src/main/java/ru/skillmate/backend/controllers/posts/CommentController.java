package ru.skillmate.backend.controllers.posts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillmate.backend.dto.errors.ErrorResponseDto;
import ru.skillmate.backend.dto.posts.response.CommentResponseDto;
import ru.skillmate.backend.dto.posts.response.PostResponseDto;
import ru.skillmate.backend.services.posts.PostService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
@Tag(name = "PostController")
public class CommentController {
    private final PostService postService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get all comments by postId"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resources were get successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<List<CommentResponseDto>> getAllComments(@PathVariable Long postId) {
        List<CommentResponseDto> responseDtoList = postService.getAllComments(postId);
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a comment"
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
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId,
                                                            @RequestParam @NotBlank(message = "Text should not be blank") String text,
                                                            Principal principal) {
        CommentResponseDto responseDto = postService.createComment(postId, text, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{commentId}")
    @Operation(
            summary = "Delete a comment"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resource was deleted successfully"),
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
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @PathVariable Long commentId,
                                           Principal principal) {
        postService.deleteComment(commentId, postId, principal.getName());
        return ResponseEntity.noContent().build();
    }

}
