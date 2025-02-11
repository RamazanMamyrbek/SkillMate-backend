package ru.skillmate.backend.exceptions;

public class InvalidConfirmationCodeException extends RuntimeException {
    public InvalidConfirmationCodeException() {
        super("Invalid email confirmation code");
    }
}
