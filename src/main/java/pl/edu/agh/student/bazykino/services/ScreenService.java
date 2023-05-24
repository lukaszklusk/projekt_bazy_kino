package pl.edu.agh.student.bazykino.services;

import org.springframework.stereotype.Service;
import pl.edu.agh.student.bazykino.model.Screen;
import pl.edu.agh.student.bazykino.repositories.ScreenRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ScreenService {

    private ScreenRepository screenRepository;

    public ScreenService(ScreenRepository screenRepository) {
        this.screenRepository = screenRepository;
    }

    public List<Screen> getAllScreens(){
        return screenRepository.findAll();
    }

    public Optional<Screen> getScreenById(long id){
        return screenRepository.findById(id);
    }

    public Screen addScreen(Screen screen){
        return screenRepository.save(screen);
    }

    public Optional<Screen> getScreenByNumber(int number){
        return screenRepository.getScreenByScreenNumber(number);
    }


}
