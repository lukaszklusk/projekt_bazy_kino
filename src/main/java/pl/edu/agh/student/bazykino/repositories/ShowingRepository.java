package pl.edu.agh.student.bazykino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.student.bazykino.model.Showing;

public interface ShowingRepository extends JpaRepository<Showing, Long> {
}
