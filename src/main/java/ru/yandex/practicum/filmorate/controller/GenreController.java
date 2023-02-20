package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotImplementedMethodException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.serviceInterface.FilmServiceable;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController extends AbstractController<Genre> {
    private FilmServiceable filmServiceable;

    @Autowired
    public GenreController(FilmServiceable filmServiceable) {
        this.filmServiceable = filmServiceable;
    }

    @Override
    ResponseEntity<Genre> create(Genre element) {
        throw new NotImplementedMethodException("not implemented");
    }

    @Override
    ResponseEntity<Genre> update(Genre element) {
        throw new NotImplementedMethodException("not implemented");
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Genre>> getAllElements() {
        return ResponseEntity.ok(filmServiceable.getGenre());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Genre> get(@PathVariable Integer id) {
        if (id != null) {
            return ResponseEntity.ok(filmServiceable.getGenre(id));
        } else throw new BadRequestException("mpa id missing");
    }

}
