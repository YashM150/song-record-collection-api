package song.record.collection.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents an artist in the catalogue")
public class Artist {

    @Schema(description = "Unique identifier", example = "1")
    private Integer id;

    @Schema(description = "Artist name", example = "Kendrick Lamar")
    @NotBlank(message = "Artist name must not be blank")
    @Size(min = 2, max = 50, message = "Artist name must be between 2 and 50 characters")
    private String artist;

    @Schema(description = "Genre", example = "Hip-Hop")
    @NotBlank(message = "Genre must not be blank")
    @Pattern(regexp = "^[a-zA-Z\\s/-]+$", message = "Genre must contain letters only")
    private String genre;

    @Schema(description = "Number of albums", example = "5")
    @NotNull(message = "Albums count is required")
    @Min(value = 0, message = "Albums cannot be negative")
    @Max(value = 100, message = "Albums cannot exceed 100")
    private Integer albums;
}