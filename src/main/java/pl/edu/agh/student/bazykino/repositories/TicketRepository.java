package pl.edu.agh.student.bazykino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.student.bazykino.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
