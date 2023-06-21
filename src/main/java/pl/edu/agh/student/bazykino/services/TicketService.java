package pl.edu.agh.student.bazykino.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.student.bazykino.model.*;
import pl.edu.agh.student.bazykino.repositories.TicketRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(long id){
        return ticketRepository.findById(id);
    }

    public List<Ticket> getTicketsForShowing(Showing showing){
        return ticketRepository.getAllByShowing(showing);
    }

    public List<Ticket> getTicketsForShowingWithStatusIn(Showing showing,
                                                         Collection<TicketStatus> status){
        return ticketRepository.getAllByShowingAndStatusIn(showing, status);
    }

    public Ticket createTicket(int seatColumn, int seatRow, Showing showing) {
        Ticket newTicket = new Ticket();
        newTicket.setSeatRow(seatRow);
        newTicket.setSeatColumn(seatColumn);
        newTicket.setShowing(showing);
        newTicket.setStatus(TicketStatus.reserved);
        return ticketRepository.saveAndFlush(newTicket);
    }

    public Ticket setTicketStatus(Ticket ticket, TicketStatus status){
        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    public int countTicketsForShowingsWithStatus(Collection<Showing> showings, Collection<TicketStatus> statuses){
        return ticketRepository.countAllByShowingInAndStatusIn(showings, statuses);
    }

    public void deleteTicket(Ticket ticket){
        ticketRepository.delete(ticket);
    }

    public List<TicketsCount> countTicketsForAllFilmsAndStatus(Collection<TicketStatus> statuses,
                                                               LocalDateTime start,
                                                               LocalDateTime end){
        return ticketRepository.getTicketCountForAllFilmsAndStatus(statuses, start, end);
    }
}
