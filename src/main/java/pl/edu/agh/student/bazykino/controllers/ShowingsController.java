package pl.edu.agh.student.bazykino.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.student.bazykino.model.Film;
import pl.edu.agh.student.bazykino.model.Screen;
import pl.edu.agh.student.bazykino.model.Showing;
import pl.edu.agh.student.bazykino.services.FilmService;
import pl.edu.agh.student.bazykino.services.ScreenService;
import pl.edu.agh.student.bazykino.services.ShowingService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ShowingsController {

    private final ShowingService showingService;
    private final FilmService filmService;
    private final ScreenService screenService;

    public ShowingsController(ShowingService showingService, FilmService filmService, ScreenService screenService) {
        this.showingService = showingService;
        this.filmService = filmService;
        this.screenService = screenService;
    }

    @GetMapping("/showings")
    public ResponseEntity<List<Showing>> getAllShowings(){
        return ResponseEntity.ok(showingService.getAllShowings());
    }

    @GetMapping("/showings/{id}")
    public ResponseEntity<Showing> getShowingById(@PathVariable long id){
        Optional<Showing> showingOptional = showingService.getShowingById(id);
        if(showingOptional.isPresent()) return ResponseEntity.ok(showingOptional.get());
        else return ResponseEntity.notFound().build();
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
        Showing newShowing = showingService.saveShowing(showing);
        if(newShowing != null){
            return ResponseEntity.ok(newShowing);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/showings/{id}")
    public ResponseEntity<Showing> editShowing(@PathVariable long id,
                                               @RequestBody Map<String, Object> payload){
        if(!payload.containsKey("film") && !payload.containsKey("screen") &&
                !payload.containsKey("dateTime")){
            return ResponseEntity.badRequest().build();
        }
        Optional<Showing> showingOptional = showingService.getShowingById(id);
        if(showingOptional.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Showing showing = showingOptional.get();

            if(payload.containsKey("film")) {
                Optional<Film> filmOptional = filmService.getFilmByTitle((String) payload.get("film"));
                if (filmOptional.isPresent()) showing.setFilm(filmOptional.get());
                else return ResponseEntity.badRequest().build();
            }

            if(payload.containsKey("screen")) {
                Optional<Screen> screenOptional = screenService.getScreenByNumber((Integer) payload.get("screen"));
                if (screenOptional.isPresent()) showing.setScreen(screenOptional.get());
                else return ResponseEntity.badRequest().build();
            }

            if(payload.containsKey("dateTime")) showing.setDateTime(LocalDateTime.parse((String) payload.get("dateTime"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            Showing newShowing = showingService.saveShowing(showing);
            if (newShowing != null) {
                return ResponseEntity.ok(newShowing);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/showings/{id}")
    public ResponseEntity<Showing> deleteShowing(@PathVariable long id){
        Optional<Showing> showingOptional = showingService.getShowingById(id);
        if(showingOptional.isEmpty()) return ResponseEntity.notFound().build();
        else{
            showingService.deleteShowing(showingOptional.get());
            return ResponseEntity.ok(showingOptional.get());
        }
    }
}
