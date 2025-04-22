package ru.skillmate.backend.controllers.resources;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.skillmate.backend.dto.resources.response.FileDto;
import ru.skillmate.backend.services.resources.ResourceService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
        String encodedFileName = URLEncoder.encode(fileDto.name(), StandardCharsets.UTF_8);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(encodedFileName))
                .header("File-Name", encodedFileName)
                .contentType(MediaType.valueOf(fileDto.contentType()))
                .body(fileDto.file());
    }
}
