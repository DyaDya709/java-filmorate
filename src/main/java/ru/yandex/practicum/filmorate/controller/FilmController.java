package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.validateService.CustomValidator;
import ru.yandex.practicum.filmorate.service.serviceInterface.FilmServiceable;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends AbstractController<Film> {
    private CustomValidator<Film> validator;
    private FilmServiceable filmServiceable;

    @Autowired
    public FilmController(CustomValidator<Film> validator, FilmServiceable filmServiceable) {
        this.validator = validator;
        this.filmServiceable = filmServiceable;
    }

    @PostMapping()
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        validator.validate(film);
        filmServiceable.create(film);
        return ResponseEntity.ok(film);
    }

    @PutMapping()
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        validator.validate(film);
        filmServiceable.upDate(film);
        return ResponseEntity.ok(film);
    }

    @GetMapping()
    ResponseEntity<List<Film>> getAllElements() {
        return ResponseEntity.ok(filmServiceable.get());
    }

    @GetMapping("/{filmId}")
    ResponseEntity<Film> get(@PathVariable(required = false, value = "filmId") Integer id) {
        if (id != null) {
            return ResponseEntity.ok(filmServiceable.get(id));
        } else {
            throw new NotFoundException("film id missing");
        }
    }

    @PutMapping("/{id}/like/{userId}")
    ResponseEntity<Boolean> addlike(@PathVariable(value = "id") Integer filmId
            , @PathVariable Integer userId) {
        if (filmId != null && userId != null) {
            return ResponseEntity.ok(filmServiceable.addLike(filmId, userId));
        } else {
            throw new NotFoundException("film id missing");
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    ResponseEntity<Boolean> deletelike(@PathVariable(value = "id") Integer filmId
            , @PathVariable Integer userId) {
        if (filmId != null && userId != null) {
            return ResponseEntity.ok(filmServiceable.removeLike(filmId, userId));
        } else {
            throw new NotFoundException("film id or user id missing");
        }
    }

    @GetMapping({"/popular", "/popular?count={count}"})
    ResponseEntity<List<Film>> getPopularFilms(@RequestParam(required = false) Integer count) {
        return ResponseEntity.ok(filmServiceable.getPopularFilms(count));
    }
}
