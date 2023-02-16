package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.sericeInterface.FilmServiceable;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private FilmServiceable filmServiceable;

    public GenreController(FilmServiceable filmServiceable) {
        this.filmServiceable = filmServiceable;
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getGenre() {
        return ResponseEntity.ok(filmServiceable.getGenre());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenre(@PathVariable Optional<Integer> id) {
        if (id.isPresent()) {
            return ResponseEntity.ok(filmServiceable.getGenre(id.get()));
        } else throw new BadRequestException("mpa id missing");
    }
}
