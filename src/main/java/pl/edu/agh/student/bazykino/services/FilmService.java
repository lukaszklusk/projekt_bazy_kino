package pl.edu.agh.student.bazykino.services;

import org.springframework.stereotype.Service;
import pl.edu.agh.student.bazykino.model.Film;
import pl.edu.agh.student.bazykino.repositories.FilmRepository;

import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<Film> getAllFilms(){
        return filmRepository.findAll();
    }

    public Film getFilmById(long id){
        return  filmRepository.getReferenceById(id);
    }

    public Film addFilm(Film film){
        return filmRepository.save(film);
    }

    public void deleteFilm(Film film){
        filmRepository.delete(film);
    }
}
