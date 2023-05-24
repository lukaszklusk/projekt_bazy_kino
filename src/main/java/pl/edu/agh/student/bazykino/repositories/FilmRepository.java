package pl.edu.agh.student.bazykino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.student.bazykino.model.Film;

import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Long> {
    Optional<Film> getFilmByTitle(String title);
}
