package ru.skillmate.backend.exceptions;

public class IllegalArgumentException extends RuntimeException{
    public IllegalArgumentException(String message) {
        super(message);
    }

    public static IllegalArgumentException userCannotRequestOwnAd(Long userId, Long adId) {
        return new IllegalArgumentException("User %s can't request exchange for own ad with id %s".formatted(userId, adId));
    }

    public static IllegalArgumentException userCannotRateThemselves() {
        return new IllegalArgumentException("Users cannot create a review for themselves");
    }

    public static IllegalArgumentException usersCannotFollowThemselves() {
        return new IllegalArgumentException("Users cannot follow themselves");
    }

    public static IllegalArgumentException passwordInvalid() {
        return new IllegalArgumentException("Invalid password");
    }
}
