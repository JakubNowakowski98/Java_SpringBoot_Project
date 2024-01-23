package jn.jn_projekt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer playlistId;

    @NotNull(message = "Id użytkownika nie może być puste")
    private Integer userId;

    @NotBlank(message = "Nazwa playlisty nie może być pusta")
    @Size(min = 2, max = 50, message = "Nazwa playlisty musi mieć od 2 do 50 znaków")
    private String playlistName;

    public Playlist() {
    }

    public Playlist(Integer userId, String playlistName) {
        this.userId = userId;
        this.playlistName = playlistName;
    }

    // Metody get i set
    public Integer getPlaylistId() {
        return playlistId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
