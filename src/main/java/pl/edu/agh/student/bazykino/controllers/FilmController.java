package pl.edu.agh.student.bazykino.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.student.bazykino.model.Film;
import pl.edu.agh.student.bazykino.model.Genre;
import pl.edu.agh.student.bazykino.services.FilmService;
import pl.edu.agh.student.bazykino.services.GenreService;

import java.util.*;

@RestController
public class FilmController {

    private final FilmService filmService;
    private final GenreService genreService;

    public FilmController(FilmService filmService, GenreService genreService) {
        this.filmService = filmService;
        this.genreService = genreService;
    }

    @GetMapping("/film")
    public ResponseEntity<List<Film>> getFilms(){
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/film/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable long id){
        Optional<Film> film = filmService.getFilmById(id);
        if(film.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(film.get());
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
        Film newFilm = filmService.saveFilm(film);
        if (newFilm != null) {
            return ResponseEntity.ok(newFilm);
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/film/{id}")
    public ResponseEntity<Film> editFilm(@PathVariable long id,
                                         @RequestBody Map<String, Object> payload){
        if(!payload.containsKey("title") && !payload.containsKey("director") &&
                !payload.containsKey("genres") && !payload.containsKey("length") &&
                !payload.containsKey("poster")){
            return ResponseEntity.badRequest().build();
        }
        Optional<Film> filmOptional = filmService.getFilmById(id);
        if(filmOptional.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Film film = filmOptional.get();
            if(payload.containsKey("director")) film.setDirector((String) payload.get("director"));
            if(payload.containsKey("title")) film.setTitle((String) payload.get("title"));
            if(payload.containsKey("genres")) {
                HashSet<Genre> genreSet = new HashSet<>();
                for (String genreStr : (List<String>) payload.get("genres")) {
                    Optional<Genre> genre = genreService.getGenreByName(genreStr);
                    if (genre.isPresent()) genreSet.add(genre.get());
                    else return ResponseEntity.badRequest().build();
                }
                film.setGenres(genreSet);
            }
            if(payload.containsKey("length")) film.setLength((Integer) payload.get("length"));
            if(payload.containsKey("poster")) film.setPoster(Base64.getDecoder().decode((String) payload.get("poster")));
            Film newFilm = filmService.saveFilm(film);
            if (newFilm != null) {
                return ResponseEntity.ok(newFilm);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }
}
