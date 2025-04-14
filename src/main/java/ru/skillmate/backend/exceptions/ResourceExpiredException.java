package ru.skillmate.backend.exceptions;

public class ResourceExpiredException extends RuntimeException{
    public ResourceExpiredException(String message) {
        super(message);
    }

    public static ResourceExpiredException resetPasswordTokenExpired(String token) {
        return new ResourceExpiredException("Reset password token %s was expired".formatted(token));
    }
}
