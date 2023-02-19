package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotImplementedMethodException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.serviceInterface.FilmServiceable;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController extends AbstractController<Rating> {
    private FilmServiceable filmServiceable;

    @Autowired
    public MpaController(FilmServiceable filmServiceable) {
        this.filmServiceable = filmServiceable;
    }

    @Override
    ResponseEntity<Rating> create(Rating element) {
        throw new NotImplementedMethodException("not implemented");
    }

    @Override
    ResponseEntity<Rating> update(Rating element) {
        throw new NotImplementedMethodException("not implemented");
    }

    @Override
    @GetMapping
    ResponseEntity<List<Rating>> getAllElements() {
        return ResponseEntity.ok(filmServiceable.getRating());
    }

    @Override
    @GetMapping("/{id}")
    ResponseEntity<Rating> get(@PathVariable Integer id) {
        if (id != null) {
            return ResponseEntity.ok(filmServiceable.getRating(id));
        } else throw new BadRequestException("mpa id missing");
    }
}
