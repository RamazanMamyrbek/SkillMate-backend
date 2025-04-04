package ru.skillmate.backend.services.posts.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.posts.response.CommentResponseDto;
import ru.skillmate.backend.dto.posts.response.PostResponseDto;
import ru.skillmate.backend.entities.posts.Comment;
import ru.skillmate.backend.entities.posts.Post;
import ru.skillmate.backend.entities.posts.PostLike;
import ru.skillmate.backend.entities.resources.Resource;
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.exceptions.ResourceAlreadyTakenException;
import ru.skillmate.backend.exceptions.ResourceNotFoundException;
import ru.skillmate.backend.exceptions.ResourcesNotMatchingException;
import ru.skillmate.backend.mappers.posts.PostMapper;
import ru.skillmate.backend.repositories.posts.CommentRepository;
import ru.skillmate.backend.repositories.posts.PostLikeRepository;
import ru.skillmate.backend.repositories.posts.PostRepository;
import ru.skillmate.backend.services.posts.PostService;
import ru.skillmate.backend.services.resources.ResourceService;
import ru.skillmate.backend.services.users.UsersService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostMapper postMapper;
    private final UsersService usersService;
    private final ResourceService resourceService;
    @Value("${minio.folders.postImages}")
    private String folderPostImages;

    @Override
    public List<PostResponseDto> getAllPosts(List<String> categories, Long userId, int page, int size) {
        List<Post> posts;
        Pageable pageable = PageRequest.of(page, size);
        if(categories == null || categories.isEmpty()) {
            posts = postRepository.findAllByOrderByCreatedAtDesc(pageable).getContent();
        } else {
            posts = postRepository.findAllByCategoriesIn(categories, pageable).getContent();
        }
        if(userId != null) {
            posts = posts.stream().filter(post -> post.getCreator().getId().equals(userId)).collect(Collectors.toList());
        }
        return posts.stream()
                .map(post -> postMapper.toPostResponseDto(post))
                .toList();
    }

    @Override
    @Transactional
    public PostResponseDto createPost(String email, String text, List<String> categories, MultipartFile image) {
        Users creator = usersService.getUserByEmail(email);
        Post post = Post
                .builder()
                .creator(creator)
                .text(text)
                .categories(categories)
                .build();
        if(image != null && !image.isEmpty()) {
            resourceService.checkImage(image);
            Resource resource = resourceService.saveResource(image, folderPostImages);
            post.setResource(resource);
        }
        return postMapper.toPostResponseDto(postRepository.save(post));
    }

    @Override
    @Transactional
    public PostResponseDto editPost(String email, Long postId, String text, List<String> categories, MultipartFile image) {
        Users creator = usersService.getUserByEmail(email);
        Post post = getPostById(postId);
        if(!post.getCreator().equals(creator)) {
            throw ResourcesNotMatchingException.postDoesNotBelongToUser(creator.getId(), post.getId());
        }
        post.setText(text);
        post.setCategories(categories);
        if(image != null && !image.isEmpty()) {
            resourceService.checkImage(image);
            Resource resourceFromDb = post.getResource();
            resourceService.updateResource(resourceFromDb.getId(), image);
        }
        return postMapper.toPostResponseDto(postRepository.save(post));
    }

    @Override
    public PostResponseDto getPost(Long postId) {
        Post post = getPostById(postId);
        return postMapper.toPostResponseDto(post);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, String email) {
        Users creator = usersService.getUserByEmail(email);
        Post post = getPostById(postId);
        if(!post.getCreator().equals(creator)) {
            throw ResourcesNotMatchingException.postDoesNotBelongToUser(creator.getId(), post.getId());
        }
        postRepository.delete(post);
    }

    @Override
    public List<CommentResponseDto> getAllComments(Long postId) {
        Post post = getPostById(postId);
        List<Comment> comments = commentRepository.findAllByPost(post);
        return comments.stream()
                .map(postMapper::toCommentResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentResponseDto createComment(Long postId, String text, String email) {
        Users user = usersService.getUserByEmail(email);
        Post post = getPostById(postId);
        Comment comment = Comment
                .builder()
                .post(post)
                .user(user)
                .text(text)
                .build();
        return postMapper.toCommentResponseDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long postId, String email) {
        Users user = usersService.getUserByEmail(email);
        Post post = getPostById(postId);
        Comment comment = getCommentById(commentId);
        checkComment(comment, post, user);
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void likePost(Long postId, String email) {
        Users user = usersService.getUserByEmail(email);
        Post post = getPostById(postId);
        if(postLikeRepository.existsByPostAndUser(post, user)) {
            throw ResourceAlreadyTakenException.likeAlreadyExists(post.getId(), user.getId());
        }
        PostLike like = PostLike
                .builder()
                .post(post)
                .user(user)
                .build();
        postLikeRepository.save(like);
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, String email) {
        Users user = usersService.getUserByEmail(email);
        Post post = getPostById(postId);
        PostLike like = postLikeRepository.findByPostAndUser(post, user).orElseThrow(
                () -> ResourceNotFoundException.postLikeNotFound(post.getId(), user.getId())
        );
        postLikeRepository.delete(like);
    }

    @Override
    public Integer getLikesCount(Long postId) {
        Post post = getPostById(postId);
        return postLikeRepository.findAllByPost(post).size();
    }

    private void checkComment(Comment comment, Post post, Users user) {
        if(!comment.getPost().equals(post)) {
            throw ResourcesNotMatchingException.commentDoesNotBelongToPost(comment.getId(), post.getId());
        }
        if(!comment.getUser().equals(user)) {
            throw ResourcesNotMatchingException.commentDoesNotBelongToUser(comment.getId(), user.getId());
        }
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> ResourceNotFoundException.commentNotFound(commentId)
        );
    }


    private Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> ResourceNotFoundException.postNotFound(postId)
        );

    }

}
