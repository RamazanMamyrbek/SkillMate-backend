package ru.skillmate.backend.exceptions;

public class ResourcesNotMatchingException extends RuntimeException{
    public ResourcesNotMatchingException(String message) {
        super(message);
    }

    public static ResourcesNotMatchingException userIdAndSkillIdNotMathing(Long userId, Long skillId) {
        return new ResourcesNotMatchingException("User id %s and skill id %s are not matching".formatted(userId, skillId));
    }

    public static ResourcesNotMatchingException skillNameNotValidForAd(String skillName, Long userId) {
        return new ResourcesNotMatchingException("Skill %s is not valid for user with id %s".formatted(skillName, userId));
    }

    public static ResourcesNotMatchingException userEmailAndIdNotMatching(String email, Long id) {
        return new ResourcesNotMatchingException("Email %s is not valid for user with id %s".formatted(email, id));
    }

    public static ResourcesNotMatchingException adDoesNottBelongToUser(Long adId, Long userId) {
        return new ResourcesNotMatchingException("Ad with id %s does not belong to user with id %s".formatted(adId, userId));
    }

    public static ResourcesNotMatchingException reviewDoesNotBelongToUser(Long reviewId, Long reviewerId) {
        return new ResourcesNotMatchingException("Review with id %s does not belong to reviewer with id %s".formatted(reviewId, reviewerId));
    }
}
