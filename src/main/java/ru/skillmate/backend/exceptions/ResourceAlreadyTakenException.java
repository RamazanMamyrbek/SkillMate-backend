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
}
