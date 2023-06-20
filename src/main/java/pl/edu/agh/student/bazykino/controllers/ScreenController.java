package pl.edu.agh.student.bazykino.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.student.bazykino.model.Screen;
import pl.edu.agh.student.bazykino.services.ScreenService;

import java.util.List;
import java.util.Map;

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
        Screen newScreen = screenService.addScreen(screen);
        if(newScreen != null){
            return ResponseEntity.ok(newScreen);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
