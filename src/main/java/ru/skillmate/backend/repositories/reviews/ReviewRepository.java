package ru.skillmate.backend.repositories.reviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillmate.backend.entities.reviews.Review;
import ru.skillmate.backend.entities.users.Users;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByReviewerAndRecipient(Users reviewer, Users recipient);

    List<Review> findAllByRecipientId(Long recipientId);
}
