package pl.edu.agh.student.bazykino.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.student.bazykino.model.Screen;
import pl.edu.agh.student.bazykino.services.ScreenService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ScreenController {

    private final ScreenService screenService;

    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
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
        Screen newScreen = screenService.saveScreen(screen);
        if(newScreen != null){
            return ResponseEntity.ok(newScreen);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/screen/{id}")
    public ResponseEntity<Screen> editScreen(@PathVariable long id,
                                             @RequestBody Map<String, Object> payload){
        if(!payload.containsKey("screenNumber") && !payload.containsKey("n_rows") &&
                !payload.containsKey("n_columns")){
            return ResponseEntity.badRequest().build();
        }
        Optional<Screen> screenOptional = screenService.getScreenById(id);
        if(screenOptional.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Screen screen = screenOptional.get();
            if(payload.containsKey("screenNumber")) screen.setScreenNumber((Integer) payload.get("screenNumber"));
            if(payload.containsKey("n_rows")) screen.setN_rows((Integer) payload.get("n_rows"));
            if(payload.containsKey("n_columns")) screen.setN_columns((Integer) payload.get("n_columns"));
            screen.setSeats_total(screen.getN_columns() * screen.getN_rows());
            Screen newScreen = screenService.saveScreen(screen);
            if (newScreen != null) {
                return ResponseEntity.ok(newScreen);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/screen/{id}")
    public ResponseEntity<Screen> deleteScreen(@PathVariable long id){
        Optional<Screen> screenOptional = screenService.getScreenById(id);
        if(screenOptional.isEmpty()) return ResponseEntity.notFound().build();
        else {
            screenService.deleteScreen(screenOptional.get());
            return ResponseEntity.ok(screenOptional.get());
        }
    }
}
