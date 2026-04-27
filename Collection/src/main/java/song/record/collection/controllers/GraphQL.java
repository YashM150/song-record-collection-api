package song.record.collection.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import song.record.collection.domain.Artist;
import song.record.collection.expectionhandler.GraphQLBadRequestException;
import song.record.collection.services.SongCollection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * ─────────────────────────────────────────────────────────
 * PARADIGM 3 – GraphQL
 * ─────────────────────────────────────────────────────────
 * Single endpoint  → /graphql
 * Playground UI    → /graphiql
 * Client picks exactly which fields it needs.
 * Schema-first: schema.graphqls is the source of truth.
 * ─────────────────────────────────────────────────────────
 */
@Controller
public class GraphQL {

    private final SongCollection songCollection;

    public GraphQL(SongCollection songCollection) {
        this.songCollection = songCollection;
    }

    // ── Query all artists ─────────────────────────────────
    @QueryMapping
    public List<Artist> artists(@Argument Integer page,
                                @Argument Integer size) {
        int p = page != null ? page : 0;
        int s = size != null ? size : 10;

        // Validate page size
        if (s > 50) {
            throw new GraphQLBadRequestException(
                    "Page size cannot exceed 50");
        }

        Pageable pageable = PageRequest.of(p, s);
        return songCollection.fetchArtist(pageable);
    }

    // ── Query one artist ──────────────────────────────────
    @QueryMapping
    public Artist artist(@Argument String artistName) {
        if (artistName == null || artistName.isBlank()) {
            throw new GraphQLBadRequestException(
                    "artistName must not be blank");
        }
        // fetchOneArtist already throws ResourceNotFoundException
        // GraphQLExceptionHandler will catch and convert it
        return songCollection.fetchOneArtist(artistName);
    }

    // ── Mutation create artist ────────────────────────────
    @MutationMapping
    public Artist createArtist(@Argument ArtistInput input) {
        if (input.artist() == null || input.artist().isBlank()) {
            throw new GraphQLBadRequestException(
                    "artist name must not be blank");
        }
        if (input.albums() < 0) {
            throw new GraphQLBadRequestException(
                    "albums cannot be negative");
        }

        Artist artist = Artist.builder()
                .artist(input.artist())
                .genre(input.genre())
                .albums(input.albums())
                .build();

        // addArtist throws DuplicateResourceException if exists
        // GraphQLExceptionHandler converts it to CONFLICT error
        return songCollection.addArtist(artist);
    }

    public record ArtistInput(String artist, String genre, Integer albums) {}
}