package jn.jn_projekt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer songId;

    @NotNull(message = "Id playlisty nie może być puste")
    private Integer playlistId;

    @NotBlank(message = "Autor nie może być pusty")
    @Size(min = 2, max = 100, message = "Autor musi mieć od 2 do 100 znaków")
    private String author;

    @NotBlank(message = "Tytuł nie może być pusty")
    @Size(min = 2, max = 100, message = "Tytuł musi mieć od 2 do 100 znaków")
    private String title;

    @NotBlank(message = "Kategoria nie może być pusta")
    @Size(min = 2, max = 50, message = "Kategoria musi mieć od 2 do 50 znaków")
    private String category;

    @NotBlank(message = "Link do YouTube nie może być pusty")
    @Pattern(regexp = "^(https://www.youtube.com/|https://youtu.be/).*", message = "Link do YouTube musi zaczynać się od 'https://www.youtube.com/' lub 'https://youtu.be/'")
    private String youtubeLink;

    public Song() {
    }

    public Song(String author, String title, String category, String youtubeLink, Integer playlistId) {
        this.playlistId = playlistId;
        this.author = author;
        this.title = title;
        this.category = category;
        this.youtubeLink = youtubeLink;
    }

    // Metody get i set
    public Integer getSongId() {
        return songId;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

}
