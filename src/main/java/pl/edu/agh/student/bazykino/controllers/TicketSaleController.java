package pl.edu.agh.student.bazykino.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.student.bazykino.model.Showing;
import pl.edu.agh.student.bazykino.model.Ticket;
import pl.edu.agh.student.bazykino.model.TicketStatus;
import pl.edu.agh.student.bazykino.services.ShowingService;
import pl.edu.agh.student.bazykino.services.TicketService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class TicketSaleController {

    private final TicketService ticketService;
    private final ShowingService showingService;

    public TicketSaleController(TicketService ticketService, ShowingService showingService) {
        this.ticketService = ticketService;
        this.showingService = showingService;
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> getAllTickets(){
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/showings/{id}/tickets")
    public ResponseEntity<List<Ticket>> getAllTicketsForShowing(@PathVariable long id){
        Optional<Showing> showingOptional = showingService.getShowingById(id);
        if(showingOptional.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(ticketService.getTicketsForShowing(showingOptional.get()));
    }

    @GetMapping("/showings/{id}/tickets/active")
    public ResponseEntity<List<Ticket>> getAllActiveTicketsForShowing(@PathVariable long id){
        Optional<Showing> showingOptional = showingService.getShowingById(id);
        if(showingOptional.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(
                ticketService.getTicketsForShowingWithStatusIn(showingOptional.get(),
                        new HashSet<>(Arrays.asList(TicketStatus.reserved, TicketStatus.checked)))
        );
    }

    @GetMapping("/showings/{id}/tickets/{tid}")
    public ResponseEntity<Ticket> getTicketInShowing(@PathVariable long id,
                                                     @PathVariable long tid){
        Optional<Showing> showingOptional = showingService.getShowingById(id);
        if(showingOptional.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Optional<Ticket> ticketOptional = ticketService.getTicketById(tid);
            if(ticketOptional.isEmpty()) return ResponseEntity.notFound().build();
            else return ResponseEntity.ok(ticketOptional.get());
        }
    }

    @PostMapping("showings/{id}/tickets")
    public ResponseEntity<Ticket> buyTicket(@PathVariable long id,
                                            @RequestBody Map<String, Object> payload){
        if(!payload.containsKey("seatRow") || !payload.containsKey("seatColumn"))
            return ResponseEntity.badRequest().build();
        int seatColumn = (Integer) payload.get("seatColumn");
        int seatRow = (Integer) payload.get("seatRow");
        Optional<Showing> showingOptional = showingService.getShowingById(id);
        if(showingOptional.isEmpty()) return ResponseEntity.notFound().build();
        else{
            if(showingOptional.get().getScreen().getN_rows() < seatRow ||
                    showingOptional.get().getScreen().getN_columns() < seatColumn)
                return ResponseEntity.badRequest().build();
            if(showingOptional.get().getDateTime().isBefore(LocalDateTime.now()))
                return ResponseEntity.badRequest().build();

            Ticket newTicket = ticketService.createTicket(
                    seatColumn, seatRow, showingOptional.get());
            if(newTicket != null) return ResponseEntity.ok(newTicket);
            else return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/tickets/{id}")
    public ResponseEntity<Ticket> changeTicketStatus(@PathVariable long id,
                                                     @RequestBody Map<String, Object> payload){
        if(!payload.containsKey("status")) return ResponseEntity.badRequest().build();
        Optional<Ticket> ticketOptional = ticketService.getTicketById(id);
        if(ticketOptional.isEmpty()) return ResponseEntity.notFound().build();
        else {
            String statusStr = (String) payload.get("status");
            if(!statusStr.equals("reserved")
                    && !statusStr.equals("cancelled")
                    && !statusStr.equals("checked"))
                return ResponseEntity.badRequest().build();
            TicketStatus status = TicketStatus.valueOf((String) payload.get("status"));
            return ResponseEntity.ok(ticketService.setTicketStatus(ticketOptional.get(),status));
        }
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<String> sqlErrorHandler(){
        return ResponseEntity.badRequest().body("Ticket already reserved");
    }

}
