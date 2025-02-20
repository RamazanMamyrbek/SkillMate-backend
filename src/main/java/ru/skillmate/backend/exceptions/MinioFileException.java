package ru.skillmate.backend.exceptions;

public class MinioFileException extends RuntimeException{
    public MinioFileException(String message) {
        super(message);
    }
}
