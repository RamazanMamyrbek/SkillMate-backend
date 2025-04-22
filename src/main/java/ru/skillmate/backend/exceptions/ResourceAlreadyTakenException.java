package ru.skillmate.backend.exceptions;

public class ResourceAlreadyTakenException extends RuntimeException{
    public ResourceAlreadyTakenException(String message) {
        super(message);
    }


    public static ResourceAlreadyTakenException userEmailWasAlreadyTaken(String email) {
        return new ResourceAlreadyTakenException("Email %s was already taken".formatted(email));
    }

    public static ResourceAlreadyTakenException userNicknameWasAlreadyTaken(String newNickname) {
        return new ResourceAlreadyTakenException("Nickname %s was already taken".formatted(newNickname));
    }

    public static ResourceAlreadyTakenException exchangeRequestAlreadyExistsByRequesterAndAd(Long userId, Long adId) {
        return new ResourceAlreadyTakenException("Exchange request with user id %s and ad id %s already exists".formatted(userId, adId));
    }

    public static ResourceAlreadyTakenException exchangeRequestAlreadyAcceptedException(Long id) {
        return new ResourceAlreadyTakenException("Exchange request with id %s was already accepted".formatted(id));
    }

    public static ResourceAlreadyTakenException exchangeRequestAlreadyDeclinedException(Long id) {
        return new ResourceAlreadyTakenException("Exchange request with id %s was already declined".formatted(id));
    }

    public static ResourceAlreadyTakenException reviewWithReviewerAndRecipientAlreadyExists(Long reviewerId, Long recipientId) {
        return new ResourceAlreadyTakenException("Review with reviewerId %s and recipientId %s does already exists".formatted(reviewerId, recipientId));
    }

    public static ResourceAlreadyTakenException likeAlreadyExists(Long postId, Long userId) {
        return new ResourceAlreadyTakenException("User with id %s has already liked the post with id %s".formatted(userId, postId));
    }

    public static ResourceAlreadyTakenException userAlreadyFollowed(Long userId, Long followingUserId) {
        return new ResourceAlreadyTakenException("User with id %s has followed user with id %s already".formatted(userId, followingUserId));
    }

    public static ResourceAlreadyTakenException userAlreadyHasSkill(Long userId, String name) {
        return new ResourceAlreadyTakenException("User with id %s already has skill with name %s".formatted(userId, name));
    }

    public static ResourceAlreadyTakenException adAlreadyExistsByNameAndUser(String skillName, Long userId) {
        return new ResourceAlreadyTakenException("Ad with skillName %s and user id %s already exists".formatted(skillName, userId));
    }
}
