package ru.skillmate.backend.annotations.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.skillmate.backend.annotations.validation.validator.TrimmedValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TrimmedValidator.class)
public @interface Trimmed {
    String message() default "The field must not start or end with spaces";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
