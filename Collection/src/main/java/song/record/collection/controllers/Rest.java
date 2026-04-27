package song.record.collection.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import song.record.collection.domain.Artist;
import song.record.collection.services.SongCollection;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/v1/api/rest/")
@Validated
@Tag(name = "REST", description = "Resource-oriented endpoints — HTTP verb encodes the action")
@SecurityRequirement(name = "basicAuth")
public class Rest {
    private final SongCollection songCollection;

    public Rest(SongCollection songCollection) {
        this.songCollection = songCollection;
    }

    @GetMapping("/artists")
    @Operation(summary = "Get all artists", description = "Returns the full catalogue of artists.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Artist.class)))),
            @ApiResponse(responseCode = "401", description = "Invalid Authentication", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<Artist>> fetchArtists(@RequestParam(defaultValue = "0")
                                         @Min(value = 0, message = "Page number cannot be negative")
                                         int page,

                                     @RequestParam(defaultValue = "10")
                                         @Min(value = 1, message = "Page size must be at least 1")
                                         @Max(value = 50, message = "Page size cannot exceed 50")
                                         int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(songCollection.fetchArtist(pageable));
    }

    @PostMapping("/artist")
    @Operation(summary = "Add artist", description = "Add the full metadata of artist.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Artist Meta data added successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Artist.class)))),
            @ApiResponse(responseCode = "401", description = "Invalid Authentication", content = @Content),
            @ApiResponse(responseCode = "409", description = "Artist Already Exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> addArtists(@Valid @RequestBody Artist artistMetaData)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(songCollection.addArtist(artistMetaData));
    }

    @GetMapping("/artist/{artistName}")
    @Operation(summary = "Get all artists", description = "Returns the full metadata of artist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artist returned successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Artist.class)))),
            @ApiResponse(responseCode = "401", description = "Invalid Authentication", content = @Content),
            @ApiResponse(responseCode = "404", description = "Artist Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> fetchOneArtist(@PathVariable @NotBlank(message = "Artist name must not be blank")
                                                @Size(min = 2, max = 50, message = "Artist name must be between 2 and 50 characters") String artistName)
    {
        return ResponseEntity.ok(songCollection.fetchOneArtist(artistName));
    }


}
