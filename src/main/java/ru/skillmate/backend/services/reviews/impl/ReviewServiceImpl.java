package ru.skillmate.backend.services.reviews.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillmate.backend.dto.reviews.request.ReviewCreateRequestDto;
import ru.skillmate.backend.dto.reviews.response.ReviewResponseDto;
import ru.skillmate.backend.entities.reviews.Review;
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.exceptions.IllegalArgumentException;
import ru.skillmate.backend.exceptions.ResourceAlreadyTakenException;
import ru.skillmate.backend.exceptions.ResourceNotFoundException;
import ru.skillmate.backend.exceptions.ResourcesNotMatchingException;
import ru.skillmate.backend.mappers.reviews.ReviewMapper;
import ru.skillmate.backend.repositories.reviews.ReviewRepository;
import ru.skillmate.backend.services.reviews.ReviewService;
import ru.skillmate.backend.services.users.UsersService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UsersService usersService;
    @Override
    public List<ReviewResponseDto> getReviewsByUser(Long userId) {
        return reviewRepository.findAllByRecipientId(userId)
                .stream()
                .map(reviewMapper::toReviewResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public ReviewResponseDto createReview(ReviewCreateRequestDto reviewCreateRequestDto, String email) {
        Users reviewer = usersService.getUserByEmail(email);
        Users recipient = usersService.getUserById(reviewCreateRequestDto.recipientId());
        if(reviewer.equals(recipient)) {
            throw IllegalArgumentException.userCannotRateThemselves();
        }
        if(reviewRepository.existsByReviewerAndRecipient(reviewer, recipient)) {
            throw ResourceAlreadyTakenException.reviewWithReviewerAndRecipientAlreadyExists(reviewer.getId(), reviewCreateRequestDto.recipientId());
        }
        Review review = reviewRepository.save(reviewMapper.mapToReviewEntity(reviewCreateRequestDto, reviewer, recipient));
        return reviewMapper.toReviewResponseDto(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, String email) {
        Users reviewer = usersService.getUserByEmail(email);
        Review review = getReviewById(reviewId);
        if(!review.getReviewer().equals(reviewer)) {
            throw ResourcesNotMatchingException.reviewDoesNotBelongToUser(review.getId(), reviewer.getId());
        }
        reviewRepository.delete(review);
    }

    private Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> ResourceNotFoundException.reviewNotFound(reviewId)
        );
    }
}
