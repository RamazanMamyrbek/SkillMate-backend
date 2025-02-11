package ru.skillmate.backend.annotations.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.skillmate.backend.annotations.validation.Trimmed;

public class TrimmedValidator implements ConstraintValidator<Trimmed, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // Разрешаем null, проверяется отдельно @NotNull
        return value.equals(value.trim()); // Проверяем, убираются ли пробелы
    }
}

