package pl.edu.agh.student.bazykino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.agh.student.bazykino.model.Genre;
import pl.edu.agh.student.bazykino.model.GenreCount;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> getGenreByName(String name);

    @Query(value =
            "SELECT new pl.edu.agh.student.bazykino.model.GenreCount(g.name, count(t))" +
                    "from Ticket t left join t.showing s left join s.film f left join f.genres g " +
                    "where s.dateTime >= :start and s.dateTime <= :end group by g.name")
    List<GenreCount> countTicketsForAllGenres(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

    @Query(value =
            "SELECT new pl.edu.agh.student.bazykino.model.GenreCount(g.name, count(t))" +
                    "from Ticket t left join t.showing s left join s.film f left join f.genres g " +
                    "where s.dateTime >= :start and s.dateTime <= :end and g.name in :genreNames group by g.name")
    List<GenreCount> countTicketsForGenresIn(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end,
                                             @Param("genreNames")Collection<String> genreNames);
}
