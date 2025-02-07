package ru.skillmate.backend.exceptions;

public class InvalidConfirmationCodeException extends RuntimeException {
    public InvalidConfirmationCodeException() {
        super("Invalid or expired email confirmation code");
    }
}
