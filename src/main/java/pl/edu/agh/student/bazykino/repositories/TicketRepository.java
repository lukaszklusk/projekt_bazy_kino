package pl.edu.agh.student.bazykino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.student.bazykino.model.Showing;
import pl.edu.agh.student.bazykino.model.Ticket;
import pl.edu.agh.student.bazykino.model.TicketStatus;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> getAllByShowing(Showing showing);
    List<Ticket> getAllByShowingAndStatusIn(Showing showing, Collection<TicketStatus> status);
}
