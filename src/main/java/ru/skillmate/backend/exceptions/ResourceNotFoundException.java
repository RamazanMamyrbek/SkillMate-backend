package ru.skillmate.backend.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.skillmate.backend.entities.users.Users;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException pendingUserNotFoundByEmail(String email) {
        return new ResourceNotFoundException("Pending user with email %s was not found".formatted(email));
    }

    public static RuntimeException pendingUserNotFoundById(Long id) {
        return new ResourceNotFoundException("Pending user with id %s was not found".formatted(id));
    }

    public static RuntimeException userNotFoundById(Long id) {
        return new ResourceNotFoundException("User with id %s was not found".formatted(id));
    }

    public static UsernameNotFoundException throwUsernameNotFoundException(String email) {
        return new UsernameNotFoundException("User with email %s was not found".formatted(email));
    }

    public static ResourceNotFoundException resourceNotFoundById(Long resourceId) {
        return new ResourceNotFoundException("Resource with id %s was not found".formatted(resourceId));
    }

    public static ResourceNotFoundException skillNotFound(Long skillId) {
        return new ResourceNotFoundException("Skill with id %s was not found".formatted(skillId));
    }

    public static ResourceNotFoundException adNotFound(Long adId) {
        return new ResourceNotFoundException("Ad with id %s was not found".formatted(adId));
    }

    public static ResourceNotFoundException userNotFoundByEmail(String email) {
        return new ResourceNotFoundException("User with email %s was not found".formatted(email));
    }

    public static ResourceNotFoundException chatNotFound(String chatId) {
        return new ResourceNotFoundException("Chat with id %s was not found".formatted(chatId));
    }

    public static ResourceNotFoundException exchangeRequestNotFoundByRequestIdAndUser(Long requestId, Users receiver) {
        return new ResourceNotFoundException("Exchange request with id %s does not aimed to user with id %s".formatted(requestId, receiver.getId()));
    }

    public static ResourceNotFoundException exchangeRequestNotFoundById(Long requestId) {
        return new ResourceNotFoundException("Exchange request with id %s was not found".formatted(requestId));
    }

    public static ResourceNotFoundException reviewNotFound(Long reviewId) {
        return new ResourceNotFoundException("Review with id %s was not found".formatted(reviewId));
    }
}
