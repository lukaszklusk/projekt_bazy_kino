package pl.edu.agh.student.bazykino.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.student.bazykino.model.Film;
import pl.edu.agh.student.bazykino.model.Genre;
import pl.edu.agh.student.bazykino.model.Screen;
import pl.edu.agh.student.bazykino.model.Showing;
import pl.edu.agh.student.bazykino.services.FilmService;
import pl.edu.agh.student.bazykino.services.GenreService;
import pl.edu.agh.student.bazykino.services.ScreenService;
import pl.edu.agh.student.bazykino.services.ShowingService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class DataController {

    private FilmService filmService;
    private GenreService genreService;
    private ScreenService screenService;
    private ShowingService showingService;

    public DataController(FilmService filmService, GenreService genreService,
                          ScreenService screenService, ShowingService showingService) {
        this.filmService = filmService;
        this.genreService = genreService;
        this.screenService = screenService;
        this.showingService = showingService;
    }

    @GetMapping("/film")
    public ResponseEntity<List<Film>> getFilms(){
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @PostMapping("/film")
    public ResponseEntity<Film> createFilm(@RequestBody Map<String, Object> payload){
        if(!payload.containsKey("title") || !payload.containsKey("director") ||
            !payload.containsKey("genres") || !payload.containsKey("length") ||
            !payload.containsKey("poster")){
            return ResponseEntity.badRequest().build();
        }
        Film film = new Film();
        film.setDirector((String) payload.get("director"));
        film.setTitle((String) payload.get("title"));
        HashSet<Genre> genreSet = new HashSet<>();
        for(String genreStr: (List<String>) payload.get("genres")){
            Optional<Genre> genre = genreService.getGenreByName(genreStr);
            if(genre.isPresent()) genreSet.add(genre.get());
            else return ResponseEntity.badRequest().build();
        }
        film.setGenres(genreSet);
        film.setLength((Integer) payload.get("length"));
        film.setPoster(Base64.getDecoder().decode((String) payload.get("poster")));
        Film newFilm = filmService.addFilm(film);
        if (newFilm != null) {
            return ResponseEntity.ok(newFilm);
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/img")
    public String getBase64Img(){
        File file = new File("./orb.jpg");
        byte[] bytes = new byte[(int) file.length()];
        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    @PostMapping("/genre")
    public ResponseEntity<Genre> addGenre(@RequestBody Map<String, Object> payload){
        if(!payload.containsKey("name")){
            return ResponseEntity.badRequest().build();
        }
        Genre genre = new Genre();
        genre.setName((String) payload.get("name"));
        Genre newGenre = genreService.addGenre(genre);
        if(newGenre != null){
            return ResponseEntity.ok(newGenre);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/genre")
    public ResponseEntity<List<Genre>> getGenres(){
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/screen")
    public ResponseEntity<List<Screen>> getScreens(){
        return ResponseEntity.ok(screenService.getAllScreens());
    }

    @PostMapping("/screen")
    public ResponseEntity<Screen> addScreen(@RequestBody Map<String, Object> payload){
        if(!payload.containsKey("screenNumber") || !payload.containsKey("n_rows") ||
            !payload.containsKey("n_columns")){
            return ResponseEntity.badRequest().build();
        }
        Screen screen = new Screen();
        screen.setScreenNumber((Integer) payload.get("screenNumber"));
        screen.setN_rows((Integer) payload.get("n_rows"));
        screen.setN_columns((Integer) payload.get("n_columns"));
        screen.setSeats_total(screen.getN_columns() * screen.getN_rows());
        Screen newScreen = screenService.addScreen(screen);
        if(newScreen != null){
            return ResponseEntity.ok(newScreen);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/showings")
    public ResponseEntity<List<Showing>> getAllShowings(){
        return ResponseEntity.ok(showingService.getAllShowings());
    }

    @PostMapping("/showings")
    public ResponseEntity<Showing> addShowing(@RequestBody Map<String, Object> payload){
        if(!payload.containsKey("film") || !payload.containsKey("screen") ||
                !payload.containsKey("dateTime")){
            return ResponseEntity.badRequest().build();
        }
        Showing showing = new Showing();

        Optional<Film> filmOptional = filmService.getFilmByTitle((String) payload.get("film"));
        if(filmOptional.isPresent()) showing.setFilm(filmOptional.get());
        else return ResponseEntity.badRequest().build();

        Optional<Screen> screenOptional = screenService.getScreenByNumber((Integer) payload.get("screen"));
        if(screenOptional.isPresent()) showing.setScreen(screenOptional.get());
        else return ResponseEntity.badRequest().build();

        showing.setDateTime(LocalDateTime.parse((String) payload.get("dateTime"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Showing newShowing = showingService.addShowing(showing);
        if(newShowing != null){
            return ResponseEntity.ok(newShowing);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
