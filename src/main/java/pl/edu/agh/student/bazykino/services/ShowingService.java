package pl.edu.agh.student.bazykino.services;

import org.springframework.stereotype.Service;
import pl.edu.agh.student.bazykino.model.Showing;
import pl.edu.agh.student.bazykino.repositories.ShowingRepository;

import java.util.List;

@Service
public class ShowingService {

    private ShowingRepository showingRepository;

    public ShowingService(ShowingRepository showingRepository) {
        this.showingRepository = showingRepository;
    }

    public List<Showing> getAllShowings(){
        return showingRepository.findAll();
    }

    public Showing addShowing(Showing showing){
        return showingRepository.save(showing);
    }
}
