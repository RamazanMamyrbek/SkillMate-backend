package ru.skillmate.backend.controllers.ads;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.skillmate.backend.dto.ads.request.ExchangeRequestDto;
import ru.skillmate.backend.dto.ads.response.ExchangeRequestDecisionDto;
import ru.skillmate.backend.dto.ads.response.ExchangeResponseDto;
import ru.skillmate.backend.dto.errors.ErrorResponseDto;
import ru.skillmate.backend.entities.ads.enums.ExchangeStatus;
import ru.skillmate.backend.services.ads.ExchangeRequestService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ads/exchange-requests")
@RequiredArgsConstructor
@Validated
@Tag(name = "ExchangeRequestController", description = "Endpoints for managing exchange requests for ads")
public class ExchangeRequestController {
    private final ExchangeRequestService exchangeRequestService;

    @GetMapping(value = "/sent", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get exchange requests sent by the current user",
            description = "Retrieves all exchange requests that the current user has sent"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requests retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<List<ExchangeResponseDto>> getSentExchangeRequests(Principal principal) {
        List<ExchangeResponseDto> requests = exchangeRequestService.getSentExchangeRequests(principal.getName());
        return ResponseEntity.ok(requests);
    }

    @GetMapping(value = "/received", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get exchange requests received for the user's ads",
            description = "Retrieves all exchange requests that other users have left on the current user's ads"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requests retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Authorization error", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponseDto.class)
            ))
    })
    public ResponseEntity<List<ExchangeResponseDto>> getReceivedExchangeRequests(Principal principal) {
        List<ExchangeResponseDto> requests = exchangeRequestService.getReceivedExchangeRequests(principal.getName());
        return ResponseEntity.ok(requests);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create an exchange request"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Request was created successfully"),
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
    public ResponseEntity<ExchangeResponseDto> createExchangeRequest(@RequestBody @Valid ExchangeRequestDto exchangeRequestDto,
                                                                     Principal principal) {
        ExchangeResponseDto responseDto = exchangeRequestService.createExchangeRequest(exchangeRequestDto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/{requestId}")
    @Operation(
            summary = "Accept or decline an exchange request"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Request was created successfully"),
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
    public ResponseEntity<ExchangeRequestDecisionDto> acceptOrDecline(@PathVariable Long requestId,
                                                                      @RequestParam ExchangeStatus status,
                                                                      Principal principal) {
        ExchangeRequestDecisionDto decisionDto = exchangeRequestService.acceptOrDecline(requestId, status, principal.getName());
        return ResponseEntity.ok(decisionDto);
    }

}
