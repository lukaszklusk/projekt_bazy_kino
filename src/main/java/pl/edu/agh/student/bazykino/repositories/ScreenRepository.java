package pl.edu.agh.student.bazykino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.student.bazykino.model.Screen;

import java.util.Optional;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
    Optional<Screen> getScreenByScreenNumber(int screenNumber);
}
