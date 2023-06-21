package pl.edu.agh.student.bazykino.services;

import org.springframework.stereotype.Service;
import pl.edu.agh.student.bazykino.model.Showing;
import pl.edu.agh.student.bazykino.repositories.ShowingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShowingService {

    private ShowingRepository showingRepository;

    public ShowingService(ShowingRepository showingRepository) {
        this.showingRepository = showingRepository;
    }

    public List<Showing> getAllShowings(){
        return showingRepository.findAll();
    }

    public Showing saveShowing(Showing showing){
        return showingRepository.save(showing);
    }

    public Optional<Showing> getShowingById(long id){
        return showingRepository.findById(id);
    }

    public void deleteShowing(Showing showing){
        showingRepository.delete(showing);
    }
}
