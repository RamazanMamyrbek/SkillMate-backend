package ru.skillmate.backend.repositories.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillmate.backend.entities.posts.Post;
import ru.skillmate.backend.entities.posts.PostLike;
import ru.skillmate.backend.entities.users.Users;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostAndUser(Post post, Users user);
    
    Optional<PostLike> findByPostAndUser(Post post, Users user);

    List<PostLike> findAllByPost(Post post);
}
