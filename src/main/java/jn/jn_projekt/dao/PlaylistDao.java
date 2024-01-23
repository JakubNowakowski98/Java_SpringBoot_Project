package jn.jn_projekt.dao;

import jakarta.transaction.Transactional;
import java.util.List;
import jn.jn_projekt.entity.Playlist;
import org.springframework.data.repository.CrudRepository;

public interface PlaylistDao extends CrudRepository<Playlist, Integer> {
    
    public Playlist findByPlaylistId(Integer playlistId);

    List<Playlist> findByUserId(Integer userId);
    
    @Transactional
    void deleteByPlaylistId(Integer playlistId);
}
