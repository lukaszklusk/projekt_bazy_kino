package pl.edu.agh.student.bazykino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.student.bazykino.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
