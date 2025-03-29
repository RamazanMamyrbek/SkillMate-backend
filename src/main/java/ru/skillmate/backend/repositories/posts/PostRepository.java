package ru.skillmate.backend.repositories.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillmate.backend.entities.posts.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("select p from Post p join p.categories c where c in :categories order by p.createdAt desc ")
    Page<Post> findAllByCategoriesIn(List<String> categories, Pageable pageable);
}
