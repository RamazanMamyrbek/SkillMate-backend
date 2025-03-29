package ru.skillmate.backend.controllers.posts;

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
import ru.skillmate.backend.dto.errors.ErrorResponseDto;
import ru.skillmate.backend.dto.posts.response.PostResponseDto;
import ru.skillmate.backend.services.posts.PostService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "PostController", description = "Endpoints for posts management")
public class PostController {
    private final PostService postService;

    @GetMapping
    @Operation(
            summary = "Get all posts"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resources were get successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<List<PostResponseDto>> getAllPosts(@RequestParam(required = false) List<String> categories,
                                                             @RequestParam(required = false) Long userId,
                                                             @RequestParam int page,
                                                             @RequestParam int size) {
        List<PostResponseDto> responseDtoList = postService.getAllPosts(categories,userId, page, size);
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a post"
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
    public ResponseEntity<PostResponseDto> createPost(@RequestParam String text,
                                                      @RequestParam List<String> categories,
                                                      @RequestParam(required = false) MultipartFile image,
                                                      Principal principal) {
        PostResponseDto responseDto = postService.createPost(principal.getName(), text, categories, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Edit a post"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource was edited successfully"),
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
    public ResponseEntity<PostResponseDto> editPost(@PathVariable Long postId,
                                                    @RequestParam String text,
                                                    @RequestParam List<String> categories,
                                                    @RequestParam(required = false) MultipartFile image,
                                                    Principal principal ) {
        PostResponseDto responseDto = postService.editPost(principal.getName(), postId, text, categories, image);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping(value = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get post by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource was get successfully"),
            @ApiResponse(responseCode = "404", description = "Resource was not found", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto responseDto = postService.getPost(postId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping(value = "/{postId}")
    @Operation(
            summary = "Delete a post"
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
                                           Principal principal) {
        postService.deletePost(postId, principal.getName());
        return ResponseEntity.noContent().build();
    }



}
