package jn.jn_projekt.dao;

import jakarta.transaction.Transactional;
import jn.jn_projekt.entity.Song;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SongDao extends CrudRepository<Song, Integer> {
    
    public Song findByYoutubeLinkAndPlaylistId(String youtubeLink, Integer playlistId);
    
    List<Song> findByPlaylistId(Integer playlistId);
    
    List<Song> findByAuthorContainingAndPlaylistId(String author, Integer playlistId);
    
    List<Song> findByTitleContainingAndPlaylistId(String title, Integer playlistId);
    
    List<Song> findByCategoryContainingAndPlaylistId(String category, Integer playlistId);
    
    @Transactional
    void deleteBySongId(Integer songId);
}
