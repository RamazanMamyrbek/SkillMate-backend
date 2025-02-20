package ru.skillmate.backend.annotations.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.skillmate.backend.annotations.validation.EnumPattern;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<EnumPattern, String> {
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumPattern constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false; // Null или пустые строки считаем недопустимыми
        }
        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(enumValue -> enumValue.name().equals(value));
    }
}
