package pl.edu.agh.student.bazykino.services;

import org.springframework.stereotype.Service;
import pl.edu.agh.student.bazykino.model.Genre;
import pl.edu.agh.student.bazykino.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre addGenre(Genre genre){
        return genreRepository.save(genre);
    }

    public Optional<Genre> getGenreByName(String name){
        return genreRepository.getGenreByName(name);
    }

    public List<Genre> getAllGenres(){
        return genreRepository.findAll();
    }

    public Optional<Genre> getGenreById(long id){
        return genreRepository.findById(id);
    }

    public Genre renameGenre(Genre genre, String newName){
        genre.setName(newName);
        return genreRepository.save(genre);
    }
}
