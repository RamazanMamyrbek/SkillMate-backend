package ru.skillmate.backend.services.posts;

import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.posts.response.PostResponseDto;

import java.util.List;

public interface PostService {
    List<PostResponseDto> getAllPosts(List<String> categories, Long userId, int page, int size);

    PostResponseDto createPost(String name, String text, List<String> categories, MultipartFile image);

    PostResponseDto editPost(String email, Long postId, String text, List<String> categories, MultipartFile image);

    PostResponseDto getPost(Long postId);

    void deletePost(Long postId, String email);
}
