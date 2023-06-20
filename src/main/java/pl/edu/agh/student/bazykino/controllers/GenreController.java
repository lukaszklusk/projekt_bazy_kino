package pl.edu.agh.student.bazykino.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.student.bazykino.model.Genre;
import pl.edu.agh.student.bazykino.services.GenreService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
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

    @GetMapping("/genre/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable long id){
        Optional<Genre> genreOptional = genreService.getGenreById(id);
        if(genreOptional.isPresent()) return ResponseEntity.ok(genreOptional.get());
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/genre/{id}")
    public ResponseEntity<Genre> editGenre(@PathVariable long id,
                                           @RequestBody Map<String,Object> payload){
        if(!payload.containsKey("name")){
            return ResponseEntity.badRequest().build();
        }
        Optional<Genre> genreOptional = genreService.getGenreById(id);
        if(genreOptional.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Genre response = genreService.renameGenre(genreOptional.get(),
                    (String) payload.get("name"));
            if(response == null) return ResponseEntity.internalServerError().build();
            else return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/genre")
    public ResponseEntity<List<Genre>> getGenres(){
        return ResponseEntity.ok(genreService.getAllGenres());
    }
}
