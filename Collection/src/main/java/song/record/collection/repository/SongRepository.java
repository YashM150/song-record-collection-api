package song.record.collection.repository;

import org.springframework.stereotype.Component;
import song.record.collection.domain.Artist;
import song.record.collection.expectionhandler.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SongRepository {

    private final AtomicInteger idSequence = new AtomicInteger(3); // starts after seed data

    private final List<Artist> store = new ArrayList<>(List.of(
            Artist.builder().id(1).artist("Kendrick Lamar").genre("Hip-Hop").albums(5).build(),
            Artist.builder().id(2).artist("SZA").genre("R&B").albums(2).build()
    ));


    public List<Artist> fetchArtist() {
        return store;
    }

    public Artist addArtist(String artist, String genre, Integer albums) {
        Artist newArtist = Artist.builder()
                .id(idSequence.getAndIncrement())
                .artist(artist)
                .genre(genre)
                .albums(albums)
                .build();
        store.add(newArtist);
        return newArtist;
    }

    public Artist findOneArtist(String artistName)
    {

        return store.stream()
                .filter(artist1 -> artistName.equals(artist1.getArtist()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Artist '" + artistName + "' not found"));
    }

    public boolean existsByArtist(String artistName) {
        return store.stream()
                .anyMatch(a -> artistName.equals(a.getArtist()));
    }
}