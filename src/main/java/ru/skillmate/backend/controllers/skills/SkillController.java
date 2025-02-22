package ru.skillmate.backend.controllers.skills;

import io.swagger.v3.oas.annotations.Operation;
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
import ru.skillmate.backend.dto.skills.response.SkillResponseDto;
import ru.skillmate.backend.entities.skills.enums.SkillLevel;
import ru.skillmate.backend.entities.users.Gender;
import ru.skillmate.backend.services.skills.SkillService;

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
            @ApiResponse(responseCode = "403", description = "Authorization error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
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
            @ApiResponse(responseCode = "404", description = "Skill was not found"),
            @ApiResponse(responseCode = "403", description = "Authorization error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
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
            @ApiResponse(responseCode = "403", description = "Authorization error"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SkillResponseDto> createSkill(@RequestParam Long userId,
                                                        @RequestParam @NotBlank @Trimmed String name,
                                                        @RequestParam @NotBlank @Trimmed String description,
                                                        @RequestParam @NotBlank @Trimmed @EnumPattern(enumClass = SkillLevel.class, message = "Valid values for skill: BEGINNER|INTERMEDIATE|PRO") String level,
                                                        @RequestParam(required = false)List<MultipartFile> achievements) {
        SkillResponseDto responseDto = skillService.createSkill(userId, name, description, level, achievements);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

//    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(
//            summary = "Edit a skill"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Skill was edited successfully"),
//            @ApiResponse(responseCode = "403", description = "Authorization error"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<SkillResponseDto> editSkill(@RequestParam Long userId,
//                                                        @RequestParam @NotBlank @Trimmed String name,
//                                                        @RequestParam @NotBlank @Trimmed String description,
//                                                        @RequestParam @NotBlank @Trimmed @EnumPattern(enumClass = SkillLevel.class, message = "Valid values for skill: BEGINNER|INTERMEDIATE|PRO") String level,
//                                                        @RequestParam(required = false)List<MultipartFile> achievements) {
//        SkillResponseDto responseDto = skillService.editSkill(userId, name, description, level, achievements);
//        return ResponseEntity.ok().body(responseDto);
//    }
}
