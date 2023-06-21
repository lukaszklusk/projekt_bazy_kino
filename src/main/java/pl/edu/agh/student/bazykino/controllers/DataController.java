package pl.edu.agh.student.bazykino.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.student.bazykino.model.*;
import pl.edu.agh.student.bazykino.services.FilmService;
import pl.edu.agh.student.bazykino.services.GenreService;
import pl.edu.agh.student.bazykino.services.ShowingService;
import pl.edu.agh.student.bazykino.services.TicketService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
public class DataController {

    private final FilmService filmService;
    private final ShowingService showingService;
    private final TicketService ticketService;
    private final GenreService genreService;

    public DataController(FilmService filmService, ShowingService showingService, TicketService ticketService, GenreService genreService) {
        this.filmService = filmService;
        this.showingService = showingService;
        this.ticketService = ticketService;
        this.genreService = genreService;
    }

    @GetMapping("/stats/film/{filmId}")
    public ResponseEntity<Map<String,Object>> getStatsForFilm(@PathVariable long filmId,
                                                  @RequestParam(required = false) Optional<String> start,
                                                  @RequestParam(required = false) Optional<String> end) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDatetime, endDateTime;
        Map<String,Object> response = new HashMap<>();
        try {
            if(start.isPresent()) startDatetime = LocalDateTime.parse(start.get(), dateTimeFormatter);
            else startDatetime = LocalDateTime.parse("1900-12-03T10:15:30", dateTimeFormatter);
            if(end.isPresent()) endDateTime = LocalDateTime.parse(end.get(), dateTimeFormatter);
            else endDateTime = LocalDateTime.parse("2900-12-03T10:15:30",dateTimeFormatter);
        }catch (DateTimeParseException e){
            response.put("Error", "Date parsing error");
            return ResponseEntity.badRequest().body(response);
        }
        Optional<Film> filmOptional = filmService.getFilmById(filmId);
        if(filmOptional.isEmpty()) return ResponseEntity.notFound().build();
        else {
            response.put("filmId", filmId);
            response.put("startDate", startDatetime);
            response.put("endDate", endDateTime);
            List<Showing> showings = showingService.getShowingsByFilm(filmOptional.get());
            response.put("showingCount", showings.size());
            response.put("ticketCount",
                    ticketService.countTicketsForShowingsWithStatus(showings,
                            Arrays.asList(TicketStatus.reserved, TicketStatus.checked)));
            response.put("ticketsChecked",
                    ticketService.countTicketsForShowingsWithStatus(showings,
                            Arrays.asList(TicketStatus.checked)));
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/stats/film/top")
    public ResponseEntity<Map<String,Object>> getTopFilms(
            @RequestParam(required = false, defaultValue = "10", name = "limit") String limitStr,
            @RequestParam(required = false) Optional<String> start,
            @RequestParam(required = false) Optional<String> end) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDatetime, endDateTime;
        int limit = 10;
        Map<String,Object> response = new HashMap<>();
        try {
            if(start.isPresent()) startDatetime = LocalDateTime.parse(start.get(), dateTimeFormatter);
            else startDatetime = LocalDateTime.parse("1900-12-03T10:15:30", dateTimeFormatter);
            if(end.isPresent()) endDateTime = LocalDateTime.parse(end.get(), dateTimeFormatter);
            else endDateTime = LocalDateTime.parse("2900-12-03T10:15:30",dateTimeFormatter);
            limit = Integer.parseInt(limitStr);
        }catch (DateTimeParseException | NumberFormatException e){
            response.put("Error", "Parsing error");
            return ResponseEntity.badRequest().body(response);
        }
        response.put("startDate", startDatetime);
        response.put("endDate", endDateTime);
        response.put("limit", limit);
        List<TicketsCount> ticketsCountList = ticketService.countTicketsForAllFilmsAndStatus(
                Arrays.asList(TicketStatus.reserved, TicketStatus.checked),
                startDatetime, endDateTime);
        ticketsCountList.sort(Comparator.comparingLong(TicketsCount::getCount));
        if(ticketsCountList.size() > 0) ticketsCountList = ticketsCountList.subList(0,Math.min(limit, ticketsCountList.size()-1));
        response.put("topList", ticketsCountList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/genre/top")
    public ResponseEntity<Map<String,Object>> getTopGenres(
            @RequestParam(required = false, defaultValue = "10", name = "limit") String limitStr,
            @RequestParam(required = false) Optional<String> start,
            @RequestParam(required = false) Optional<String> end) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDatetime, endDateTime;
        Map<String,Object> response = new HashMap<>();
        int limit = 10;
        try {
            if(start.isPresent()) startDatetime = LocalDateTime.parse(start.get(), dateTimeFormatter);
            else startDatetime = LocalDateTime.parse("1900-12-03T10:15:30", dateTimeFormatter);
            if(end.isPresent()) endDateTime = LocalDateTime.parse(end.get(), dateTimeFormatter);
            else endDateTime = LocalDateTime.parse("2900-12-03T10:15:30",dateTimeFormatter);
            limit = Integer.parseInt(limitStr);
        }catch (DateTimeParseException e){
            response.put("Error", "Date parsing error");
            return ResponseEntity.badRequest().body(response);
        }
        response.put("startDate", startDatetime);
        response.put("endDate", endDateTime);
        response.put("limit", limit);
        List<GenreCount> genreCountList =
                genreService.countTicketsForAllGenres(startDatetime, endDateTime);
        genreCountList.sort(Comparator.comparingLong(GenreCount::getCount));
        if(genreCountList.size() > 0)genreCountList.subList(0, Math.min(limit, genreCountList.size()-1));
        response.put("topList", genreCountList);
        return ResponseEntity.ok(response);
    }

    //Test method
    @GetMapping("/img")
    public String getBase64Img() {
        File file = new File("./testimage.png");
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
}
