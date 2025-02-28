package ru.skillmate.backend.dto.common;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        int currentPage,
        int totalPages,
        long totalElements,
        int elementsPerPage
) {
}
