package pl.edu.agh.student.bazykino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.student.bazykino.model.Film;
import pl.edu.agh.student.bazykino.model.Showing;

import java.util.List;

public interface ShowingRepository extends JpaRepository<Showing, Long> {
    List<Showing> getAllByFilm(Film film);
}
