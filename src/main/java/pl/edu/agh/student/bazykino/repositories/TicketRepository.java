package pl.edu.agh.student.bazykino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.agh.student.bazykino.model.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> getAllByShowing(Showing showing);
    List<Ticket> getAllByShowingAndStatusIn(Showing showing, Collection<TicketStatus> status);
    int countAllByShowingInAndStatusIn(Collection<Showing> showing, Collection<TicketStatus> status);

    @Query(
            value = "SELECT new pl.edu.agh.student.bazykino.model.TicketsCount(f.title, count(t))" +
                    "from Ticket t left join t.showing s left join s.film f " +
                    "where t.status in :status and s.dateTime >= :start and s.dateTime <= :end group by f.title "
    )
    List<TicketsCount> getTicketCountForAllFilmsAndStatus(@Param("status") Collection<TicketStatus> status,
                                                          @Param("start") LocalDateTime start,
                                                          @Param("end") LocalDateTime end);
}
