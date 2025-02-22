package ru.skillmate.backend.exceptions;

public class ResourcesNotMatchingException extends RuntimeException{
    public ResourcesNotMatchingException(String message) {
        super(message);
    }

    public static ResourcesNotMatchingException userIdAndSkillIdNotMathing(Long userId, Long skillId) {
        return new ResourcesNotMatchingException("User id %s and skill id %s are not matching".formatted(userId, skillId));
    }
}
