package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.serviceInterface.FilmServiceable;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private FilmServiceable filmServiceable;

    public MpaController(FilmServiceable filmServiceable) {
        this.filmServiceable = filmServiceable;
    }

    @GetMapping
    ResponseEntity<List<Rating>> getRating() {
        return ResponseEntity.ok(filmServiceable.getRating());
    }
    @GetMapping("/{id}")
    ResponseEntity<Rating> getRating(@PathVariable Optional<Integer> id) {
        if (id.isPresent()) {
            return ResponseEntity.ok(filmServiceable.getRating(id.get()));
        } else throw new BadRequestException("mpa id missing");
    }
}
