package ru.skillmate.backend.controllers.ads;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillmate.backend.dto.ads.response.AdResponseDto;
import ru.skillmate.backend.dto.common.PageResponseDto;
import ru.skillmate.backend.dto.errors.ErrorResponseDto;
import ru.skillmate.backend.services.ads.AdService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ads")
@Validated
@Tag(name = "AdController", description = "Endpoints for ad management")
public class AdController {
    private final AdService adService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Search all ads",
            parameters = {
                    @Parameter(name = "page", description = "Page number(1-based)", example = "1"),
                    @Parameter(name = "size", description = "Items per page", example = "10"),
                    @Parameter(name = "searchValue", description = "Search value for searching", required = false),
                    @Parameter(name = "country", description = "Filter for country", required = false),
                    @Parameter(name = "city", description = "Filter for city", required = false),
                    @Parameter(name = "level", description = "Filter for skill level", required = false)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ads were get successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<PageResponseDto<AdResponseDto>> getAll(@RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(required = false) String searchValue,
                                                                 @RequestParam(required = false) String country,
                                                                 @RequestParam(required = false) String city,
                                                                 @RequestParam(required = false) String level) {
        PageResponseDto<AdResponseDto> responseDtoList = adService.searchAds(PageRequest.of(page-1, size), searchValue, country, city, level);
        return ResponseEntity.ok().body(responseDtoList);
    }

    @GetMapping(value = "/{adId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get ad by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ad was get successfully"),
            @ApiResponse(responseCode = "404", description = "Ad was not found", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<AdResponseDto> getAdById(@PathVariable Long adId) {
        AdResponseDto responseDto = adService.getAdResponseDtoById(adId);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create an ad"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ad was created successfully"),
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
    public ResponseEntity<AdResponseDto> createAd(@RequestParam Long userId,
                                                  @RequestParam @NotBlank String skillName,
                                                  @RequestParam @NotBlank @Size(min = 20) String description,
                                                  @RequestParam MultipartFile imageResource,
                                                  Principal principal) {
        AdResponseDto response = adService.createAd(userId, skillName, description, imageResource, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{adId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Edit an ad"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ad was edited successfully"),
            @ApiResponse(responseCode = "404", description = "Ad was not found", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )),
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
    public ResponseEntity<AdResponseDto> createAd(@PathVariable Long adId,
                                                  @RequestParam Long userId,
                                                  @RequestParam @NotBlank String skillName,
                                                  @RequestParam @NotBlank @Size(min = 20) String description,
                                                  @RequestParam(required = false) MultipartFile imageResource,
                                                  Principal principal) {
        AdResponseDto response = adService.editAd(adId, userId, skillName, description, imageResource, principal.getName());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/{adId}")
    @Operation(
            summary = "Delete an ad"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ad was deleted successfully"),
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
    public ResponseEntity<Void> deleteAd(@PathVariable Long adId, Principal principal) {
        adService.deleteAd(adId, principal.getName());
        return ResponseEntity.noContent().build();
    }

}
