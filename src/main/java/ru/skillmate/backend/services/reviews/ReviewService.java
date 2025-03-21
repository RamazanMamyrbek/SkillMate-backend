package ru.skillmate.backend.services.reviews;

import ru.skillmate.backend.dto.reviews.request.ReviewCreateRequestDto;
import ru.skillmate.backend.dto.reviews.response.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    List<ReviewResponseDto> getReviewsByUser(Long userId);

    ReviewResponseDto createReview(ReviewCreateRequestDto reviewCreateRequestDto, String email);

    void deleteReview(Long reviewId, String email);
}
