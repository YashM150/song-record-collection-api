package song.record.collection.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import song.record.collection.domain.Artist;
import song.record.collection.expectionhandler.DuplicateResourceException;
import song.record.collection.repository.SongRepository;

import java.util.List;

@Service
public class SongCollection implements SongCollectionServiceImp {

    private final SongRepository songRepository;

    public SongCollection(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public List<Artist> fetchArtist(Pageable paginate) {
        List<Artist> all = songRepository.fetchArtist();


        int start = (int) paginate.getOffset();
        int end   = Math.min(start + paginate.getPageSize(), all.size());

        if (start >= all.size()) return List.of();
        return all.subList(start, end);
    }

    @Override
    public Artist addArtist(Artist artist) {
        // 409 — duplicate check
        if (songRepository.existsByArtist(artist.getArtist())) {
            throw new DuplicateResourceException(
                    "Artist '" + artist.getArtist() + "' already exists");
        }
        return songRepository.addArtist(
                artist.getArtist(),
                artist.getGenre(),
                artist.getAlbums());
    }

    @Override
    public Artist fetchOneArtist(String artistName) {
        return songRepository.findOneArtist(artistName);
    }
}