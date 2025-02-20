package ru.skillmate.backend.annotations.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.skillmate.backend.annotations.validation.validator.EnumValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumPattern {
    Class<? extends Enum<?>> enumClass();
    String message() default "Value is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
