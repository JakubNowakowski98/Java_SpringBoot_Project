package jn.jn_projekt.controllers;

import jakarta.validation.Valid;
import jn.jn_projekt.dao.UserDao;
import jn.jn_projekt.entity.User;
import java.security.Principal;
import java.util.List;
import jn.jn_projekt.dao.PlaylistDao;
import jn.jn_projekt.dao.SongDao;
import jn.jn_projekt.entity.Playlist;
import jn.jn_projekt.entity.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDao dao;
    @Autowired
    private PlaylistDao playlistDao;
    @Autowired
    private SongDao songDao;

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Błędny login lub hasło. Spróbuj ponownie lub zarejestruj się, jeśli nie masz jeszcze konta.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model m) {
        //dodanie do modelu nowego użytkownika
        m.addAttribute("user", new User());
        //zwrócenie nazwy widoku rejestracji - register.html
        return "register";
    }

    @PostMapping("/register")
    public String registerPagePOST(@ModelAttribute @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register"; // Jeśli są błędy, wróć do formularza rejestracji
        }

        // Sprawdź, czy użytkownik o podanym loginie już istnieje w bazie danych
        User existingUser = dao.findByLogin(user.getLogin());
        if (existingUser != null) {
            bindingResult.rejectValue("login", "error.user", "Użytkownik o podanym loginie już istnieje");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);

        // Przekierowanie do adresu url: /login
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profilePage(Model m, Principal principal) {
        //dodanie do modelu aktualnie zalogowanego użytkownika:
        m.addAttribute("user", dao.findByLogin(principal.getName()));
        //zwrócenie nazwy widoku profilu użytkownika - profile.html
        return "profile";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<User> users = (List<User>) dao.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/editProfile")
    public String editProfilePage(Model model, Principal principal) {
        User user = dao.findByLogin(principal.getName());
        model.addAttribute("user", user);
        return "editProfile";
    }

    @PostMapping("/editProfile")
    public String editProfilePagePOST(@ModelAttribute @Valid User updatedUser, BindingResult bindingResult, Principal principal) {
        boolean loginChanged = false;
// Poniżej znajduje się walidacja. Jeśli są błędy, wróć do formularza edycji profilu
        if (bindingResult.hasErrors()) {
            return "editProfile";
        }

        User currentUser = dao.findByLogin(principal.getName());

        // Jeśli login został zmieniony, sprawdź, czy nowy login jest dostępny
        if (!currentUser.getLogin().equals(updatedUser.getLogin())) {
            User existingUser = dao.findByLogin(updatedUser.getLogin());
            loginChanged = true;
            if (existingUser != null) {
                bindingResult.rejectValue("login", "error.user", "Użytkownik o podanym loginie już istnieje");
                return "editProfile";
            }
        }

        // Edycja danych użytkownika
        currentUser.setName(updatedUser.getName());
        currentUser.setSurname(updatedUser.getSurname());
        currentUser.setLogin(updatedUser.getLogin());
        currentUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        dao.save(currentUser);
        if (loginChanged) {
            return "redirect:/logout";
        } else {
            return "redirect:/profile";
        }
    }

    @GetMapping("/deleteAccount")
    public String deleteAccountPage() {
        return "deleteAccount";
    }

    @PostMapping("/deleteAccountConfirmed")
    public String deleteAccountConfirmedPage(Principal principal) {
        // Tutaj dokonaj rzeczywistego usunięcia konta
        dao.deleteByLogin(principal.getName());

        // Przekierowanie na stronę potwierdzającą usunięcie konta
        return "redirect:/logout";
    }

    @GetMapping("/deleteAccountConfirmed")
    public String deleteAccountConfirmedPage() {
        return "deleteAccountConfirmed";
    }

    @GetMapping("/playlists")
    public String showPlaylists(Model model, Principal principal) {
        User currentUser = dao.findByLogin(principal.getName());
        List<Playlist> playlists = (List<Playlist>) playlistDao.findByUserId(currentUser.getUserid());
        model.addAttribute("playlists", playlists);
        return "playlists";
    }

    @GetMapping("/addPlaylist")
    public String addPlaylistPage(Model m, Principal principal) {
        m.addAttribute("playlist", new Playlist());
        User currentUser = dao.findByLogin(principal.getName());
        m.addAttribute("user", currentUser);
        return "addPlaylist";
    }

    @PostMapping("/addPlaylist")
    public String addPlaylistPagePOST(@ModelAttribute @Valid Playlist playlist, BindingResult bindingResult, Model m) {
        if (bindingResult.hasErrors()) {
            m.addAttribute("user", dao.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName()));  // Przywróć informacje o użytkowniku do modelu
            return "addPlaylist";
        }

        playlistDao.save(playlist);

        return "redirect:/playlists";
    }

    @PostMapping("/deletePlaylist/{playlistId}")
    public String deletePlaylistPage(@PathVariable Integer playlistId) {
        playlistDao.deleteByPlaylistId(playlistId);
        return "redirect:/playlists";
    }

    @GetMapping("/editPlaylist/{playlistId}")
    public String editPlaylistPage(Model model, @PathVariable Integer playlistId) {
        Playlist playlist = playlistDao.findByPlaylistId(playlistId);

        model.addAttribute("playlist", playlist);
        return "editPlaylist";
    }

    @PostMapping("/editPlaylist/{playlistId}")
    public String editPlaylistPagePOST(@ModelAttribute @Valid Playlist updatedPlaylist, BindingResult bindingResult, @PathVariable Integer playlistId, Model m) {
        if (bindingResult.hasErrors()) {
            m.addAttribute("user", dao.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName()));  // Przywróć informacje o użytkowniku do modelu
            return "editPlaylist";
        }

        Playlist currentPlaylist = playlistDao.findByPlaylistId(playlistId);

        currentPlaylist.setUserId(updatedPlaylist.getUserId());
        currentPlaylist.setPlaylistId(updatedPlaylist.getPlaylistId());
        currentPlaylist.setPlaylistName(updatedPlaylist.getPlaylistName());

        playlistDao.save(currentPlaylist);

        return "redirect:/playlists";
    }

    @GetMapping("/playlist/{playlistId}")
    public String showPlaylist(Model model, @PathVariable Integer playlistId) {
        Playlist playlist = playlistDao.findByPlaylistId(playlistId);
        List<Song> songs = songDao.findByPlaylistId(playlistId);
        Song newSong = new Song();

        model.addAttribute("playlist", playlist);
        model.addAttribute("songs", songs);
        model.addAttribute("newSong", newSong);

        return "playlist";
    }

    @GetMapping("/playlist/{playlistId}/searchSongs")
    public String searchSongs(@PathVariable Integer playlistId,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "category", required = false) String category,
            Model model) {

        List<Song> songs;

        if (title != null && !title.isEmpty()) {
            songs = songDao.findByTitleContainingAndPlaylistId(title, playlistId);
        } else if (author != null && !author.isEmpty()) {
            songs = songDao.findByAuthorContainingAndPlaylistId(author, playlistId);
        } else if (category != null && !category.isEmpty()) {
            songs = songDao.findByCategoryContainingAndPlaylistId(category, playlistId);
        } else {
            // Brak parametrów wyszukiwania, zwróć wszystkie piosenki w playliście
            songs = songDao.findByPlaylistId(playlistId);
        }

        Playlist playlist = playlistDao.findByPlaylistId(playlistId);
        model.addAttribute("playlist", playlist);
        model.addAttribute("songs", songs);
        model.addAttribute("newSong", new Song());

        return "playlist";
    }

    @GetMapping("/playlist/{playlistId}/addSong")
    public String addSongToPlaylistPage(Model model, @PathVariable Integer playlistId) {
        Playlist playlist = playlistDao.findByPlaylistId(playlistId);
        Song song = new Song();
        model.addAttribute("playlist", playlist);
        model.addAttribute("song", song);
        return "addSong";
    }

    @PostMapping("/playlist/{playlistId}/addSong")
    public String addSongToPlaylistPagePOST(@ModelAttribute @Valid Song song, BindingResult bindingResult, @PathVariable Integer playlistId, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("playlist", playlistDao.findByPlaylistId(playlistId));  // Przywróć informacje o użytkowniku do modelu
            return "addSong";
        }

        Song existingSong = songDao.findByYoutubeLinkAndPlaylistId(song.getYoutubeLink(), playlistId);

        if (existingSong != null) {
            bindingResult.rejectValue("youtubeLink", "error.youtubeLink", "Piosenka jest już dodana do Playlisty.");
            model.addAttribute("playlist", playlistDao.findByPlaylistId(playlistId));  // Przywróć informacje o użytkowniku do modelu
            return "addSong";
        }
        Playlist playlist = playlistDao.findByPlaylistId(playlistId);
        song.setPlaylistId(playlistId);
        songDao.save(song);

        return "redirect:/playlist/{playlistId}";
    }

    @GetMapping("/playlist/{playlistId}/editSong/{songId}")
    public String editSongPage(Model model, @PathVariable Integer playlistId, @PathVariable Integer songId) {
        Song song = songDao.findById(songId).orElse(null);

        if (song == null || !song.getPlaylistId().equals(playlistId)) {
            // Piosenka o danym ID nie istnieje w danej playliście
            return "redirect:/playlist/{playlistId}";
        }

        model.addAttribute("song", song);
        return "editSong";
    }

    @PostMapping("/playlist/{playlistId}/editSong/{songId}")
    public String editSongPagePOST(@ModelAttribute @Valid Song updatedSong, BindingResult bindingResult,
            @PathVariable Integer playlistId, @PathVariable Integer songId, Model model) {
        Song currentSong = songDao.findById(songId).orElse(null);

        if (currentSong == null || !currentSong.getPlaylistId().equals(playlistId)) {
            // Piosenka o danym ID nie istnieje w danej playliście
            return "redirect:/playlist/{playlistId}";
        }

        // Edycja piosenki
        currentSong.setAuthor(updatedSong.getAuthor());
        currentSong.setTitle(updatedSong.getTitle());
        currentSong.setCategory(updatedSong.getCategory());
        currentSong.setYoutubeLink(updatedSong.getYoutubeLink());

        // Sprawdzanie, czy piosenka o podanym linku YouTube już istnieje w danej playliście
        Song existingSong = songDao.findByYoutubeLinkAndPlaylistId(updatedSong.getYoutubeLink(), playlistId);
        if (existingSong != null && !existingSong.getSongId().equals(songId)) {
            bindingResult.rejectValue("youtubeLink", "error.youtubeLink", "Piosenka jest już dodana do Playlisty.");
            model.addAttribute("playlistId", playlistId);
            return "editSong";
        }

        songDao.save(currentSong);

        return "redirect:/playlist/{playlistId}";
    }

    @PostMapping("/playlist/{playlistId}/deleteSong/{songId}")
    public String deleteSongPage(@PathVariable Integer playlistId, @PathVariable Integer songId) {
        songDao.deleteBySongId(songId);
        return "redirect:/playlist/{playlistId}";
    }

}
