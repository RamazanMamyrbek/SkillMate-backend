package ru.skillmate.backend.controllers.resources;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillmate.backend.dto.resources.response.FileDto;
import ru.skillmate.backend.services.resources.ResourceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resources")
@Validated
@Tag(name = "ResourceController", description = "Endpoints for resources")
public class ResourceController {
    private final ResourceService resourceService;
    @GetMapping("/{resourceId}")
    public ResponseEntity<InputStreamResource> getResource(@PathVariable("resourceId") Long resourceId) {
        FileDto fileDto = resourceService.getFileDtoById(resourceId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(fileDto.name()))
                .contentType(MediaType.valueOf(fileDto.contentType()))
                .body(fileDto.file());
    }
}
