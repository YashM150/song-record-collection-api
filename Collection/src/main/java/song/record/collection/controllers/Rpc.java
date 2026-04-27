package song.record.collection.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import song.record.collection.domain.Artist;
import song.record.collection.expectionhandler.ResourceNotFoundException;
import song.record.collection.services.SongCollection;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/rpc")
@Tag(name = "RPC", description = "Action-oriented endpoints — procedure name is in the URL, everything is POST")
@SecurityRequirement(name = "basicAuth")
public class Rpc {

    private final SongCollection songCollection;

    public Rpc(SongCollection songCollection) {
        this.songCollection = songCollection;
    }


    @PostMapping("/artists")
    @Operation(
            summary     = "listArtists",
            description = "RPC call to fetch all artists. Body: { \"page\": 0, \"size\": 10 }")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artists returned in result envelope",
                    content = @Content(schema = @Schema(implementation = RpcResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid Authentication", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> listArtists(
            @RequestBody ListArtistsRequest request) {

        int page = request.page() != null ? request.page() : 0;
        int size = request.size() != null ? request.size() : 10;

        Pageable pageable = PageRequest.of(page, size);

        List<Artist> artists = songCollection.fetchArtist(pageable);

        return ResponseEntity.ok(Map.of(
                "result", artists,
                "page",   page,
                "size",   size,
                "count",  artists.size()
        ));
    }

    @PostMapping("/artist")
    @Operation(
            summary     = "getArtist",
            description = "RPC call to fetch a single artist. Body: { \"artistName\": \"SZA\" }")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artist returned in result envelope",
                    content = @Content(schema = @Schema(implementation = Artist.class))),
            @ApiResponse(responseCode = "401", description = "Invalid Authentication", content = @Content),
            @ApiResponse(responseCode = "404", description = "Artist Not Found",       content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",  content = @Content)
    })
    public ResponseEntity<Map<String, Object>> getArtist(
            @RequestBody GetArtistRequest request) {

        if (request.artistName() == null || request.artistName().isBlank()) {
            throw new ResourceNotFoundException(
                    "Artist '" + request.artistName + "' not found");
        }

        Artist artist = songCollection.fetchOneArtist(request.artistName());
        return ResponseEntity.ok(Map.of("result", artist));
    }

    // ── POST /createArtist ────────────────────────────────
    // REST equivalent: POST /artist
    @PostMapping("/createArtist")
    @Operation(
            summary     = "createArtist",
            description = "RPC call to create a new artist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artist created in result envelope",
                    content = @Content(schema = @Schema(implementation = Artist.class))),
            @ApiResponse(responseCode = "400", description = "Validation Failed",      content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid Authentication", content = @Content),
            @ApiResponse(responseCode = "409", description = "Artist Already Exists",  content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",  content = @Content)
    })
    public ResponseEntity<Map<String, Object>> createArtist(
            @Valid @RequestBody Artist artist) {

        Artist saved = songCollection.addArtist(artist);
        return ResponseEntity.ok(Map.of(
                "result",  saved,
                "message", "Artist created successfully"
        ));
    }

    // ── Request DTOs ──────────────────────────────────────

    @Schema(description = "Input for listArtists RPC call")
    public record ListArtistsRequest(
            @Schema(description = "Page number", example = "0") Integer page,
            @Schema(description = "Page size",   example = "10") Integer size
    ) {}

    @Schema(description = "Input for getArtist RPC call")
    public record GetArtistRequest(
            @Schema(description = "Name of the artist", example = "SZA", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank String artistName
    ) {}

    // Envelope schema for Swagger docs
    @Schema(description = "Standard RPC response envelope")
    public record RpcResponse(
            @Schema(description = "Returned data") Object result,
            @Schema(description = "Success message") String message
    ) {}
}