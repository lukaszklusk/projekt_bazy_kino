package pl.edu.agh.student.bazykino.services;

import org.springframework.stereotype.Service;
import pl.edu.agh.student.bazykino.model.Showing;
import pl.edu.agh.student.bazykino.model.Ticket;
import pl.edu.agh.student.bazykino.model.TicketStatus;
import pl.edu.agh.student.bazykino.repositories.TicketRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
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

    public Ticket createTicket(int seatColumn, int seatRow, Showing showing){
        Ticket newTicket = new Ticket();
        newTicket.setSeatRow(seatRow);
        newTicket.setSeatColumn(seatColumn);
        newTicket.setShowing(showing);
        newTicket.setStatus(TicketStatus.reserved);
        return ticketRepository.save(newTicket);
    }

    public Ticket checkTicket(Ticket ticket){
        ticket.setStatus(TicketStatus.checked);
        return ticketRepository.save(ticket);
    }

    public Ticket cancelTicket(Ticket ticket){
        ticket.setStatus(TicketStatus.cancelled);
        return ticketRepository.save(ticket);
    }
}
