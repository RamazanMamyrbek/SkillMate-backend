package ru.skillmate.backend.dto.posts.response;

public record CommentResponseDto(
        Long id,
        String text,
        Long postId,
        Long userId
) {
}
