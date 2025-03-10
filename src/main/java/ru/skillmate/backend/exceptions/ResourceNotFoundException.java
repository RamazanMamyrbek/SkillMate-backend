package ru.skillmate.backend.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
}
