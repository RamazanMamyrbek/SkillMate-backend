package ru.skillmate.backend.controllers.skills;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.annotations.validation.EnumPattern;
import ru.skillmate.backend.annotations.validation.Trimmed;
import ru.skillmate.backend.dto.errors.ErrorResponseDto;
import ru.skillmate.backend.dto.skills.response.SkillResponseDto;
import ru.skillmate.backend.entities.skills.enums.SkillLevel;
import ru.skillmate.backend.services.skills.SkillService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/skills")
@Validated
@Tag(name = "SkillController", description = "Endpoints for skill management")
public class SkillController {
    private final SkillService skillService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get all skills",
            description = "Gets all skills or skills by userId"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skills were get successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<List<SkillResponseDto>> getSkillsByUserId(@RequestParam(required = false) Long userId) {
        List<SkillResponseDto> responseDtoList = skillService.getAllSkillsByUserId(userId);
        return ResponseEntity.ok().body(responseDtoList);
    }

    @GetMapping(value = "/{skillId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get skill by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill was get successfully"),
            @ApiResponse(responseCode = "404", description = "Skill was not found", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<SkillResponseDto> getSkillById(@PathVariable Long skillId) {
        SkillResponseDto responseDto = skillService.getSkillResponseById(skillId);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a skill"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Skill was created successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<SkillResponseDto> createSkill(@RequestParam Long userId,
                                                        @RequestParam @NotBlank @Trimmed String name,
                                                        @RequestParam @NotBlank @Trimmed String description,
                                                        @RequestParam @NotBlank @Trimmed @EnumPattern(enumClass = SkillLevel.class, message = "Valid values for skill: BEGINNER|INTERMEDIATE|PRO") String level,
                                                        @RequestParam(required = false)List<MultipartFile> achievements,
                                                        Principal principal) {
        SkillResponseDto responseDto = skillService.createSkill(userId, name, description, level, achievements, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping(value = "/{skillId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Edit a skill"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill was edited successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<SkillResponseDto> editSkill(@RequestParam Long userId,
                                                      @PathVariable Long skillId,
                                                      @RequestParam @NotBlank @Trimmed String name,
                                                      @RequestParam @NotBlank @Trimmed String description,
                                                      @RequestParam @NotBlank @Trimmed @EnumPattern(enumClass = SkillLevel.class, message = "Valid values for skill: BEGINNER|INTERMEDIATE|PRO") String level,
                                                      @RequestParam(required = false)List<MultipartFile> achievements,
                                                      Principal principal) {
        SkillResponseDto responseDto = skillService.editSkill(userId,skillId, name, description, level, achievements, principal.getName());
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping(value = "/{skillId}")
    @Operation(
            summary = "Delete a skill"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Skill was deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<Void> deleteSkillById(@PathVariable Long skillId, Principal principal) {
        skillService.deleteSkillById(skillId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
