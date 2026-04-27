package song.record.collection.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import song.record.collection.domain.Artist;

import java.util.List;

public interface SongCollectionServiceImp {


    List<Artist> fetchArtist(Pageable paginate);


    Artist addArtist(Artist artist);

    Artist fetchOneArtist(String artistName);
}