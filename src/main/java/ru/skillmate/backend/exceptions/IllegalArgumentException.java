package ru.skillmate.backend.exceptions;

public class IllegalArgumentException extends RuntimeException{
    public IllegalArgumentException(String message) {
        super(message);
    }

    public static IllegalArgumentException userCannotRequestOwnAd(Long userId, Long adId) {
        return new IllegalArgumentException("User %s can't request exchange for own ad with id %s".formatted(userId, adId));
    }
}
