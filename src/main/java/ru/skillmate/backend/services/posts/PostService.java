package ru.skillmate.backend.services.posts;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.common.PageResponseDto;
import ru.skillmate.backend.dto.posts.response.CommentResponseDto;
import ru.skillmate.backend.dto.posts.response.PostResponseDto;

import java.util.List;

public interface PostService {
    List<PostResponseDto> getAllPosts(List<String> categories, Long userId, int page, int size);

    PostResponseDto createPost(String name, String text, List<String> categories, MultipartFile image);

    PostResponseDto editPost(String email, Long postId, String text, List<String> categories, MultipartFile image);

    PostResponseDto getPost(Long postId);

    void deletePost(Long postId, String email);

    PageResponseDto<CommentResponseDto> getAllComments(Long postId, PageRequest pageRequest);

    CommentResponseDto createComment(Long postId, String text, String email);

    void deleteComment(Long commentId, Long postId, String email);

    void likePost(Long postId, String email);

    void unlikePost(Long postId, String email);

    Integer getLikesCount(Long postId);

    boolean isLiked(Long postId, String email);
}
