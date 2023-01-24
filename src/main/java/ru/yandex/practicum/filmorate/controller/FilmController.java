package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.CustomValidator;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends AbstractController<Film> {
    CustomValidator<Film> validator;
    FilmService filmService;

    @Autowired
    public FilmController(CustomValidator<Film> validator, FilmService filmService) {
        this.validator = validator;
        this.filmService = filmService;
    }

    @PostMapping()
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) throws ValidationException {
        validator.validate(film);
        filmService.create(film);
        return ResponseEntity.ok(film);
    }

    @PutMapping()
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) throws ValidationException, NotFoundException {
        validator.validate(film);
        filmService.upDate(film);
        return ResponseEntity.ok(film);
    }

    @GetMapping()
    ResponseEntity<List<Film>> getAllElements() {
        return ResponseEntity.ok(filmService.get());
    }

    @GetMapping("/{filmId}")
    ResponseEntity<Film> get(@PathVariable(required = false, value = "filmId") Integer id) throws NotFoundException {
        if (id != null) {
            return ResponseEntity.ok(filmService.get(id));
        } else {
            throw new NotFoundException("film id missing");
        }
    }

    @PutMapping("/{id}/like/{userId}")
    ResponseEntity<Boolean> addlike(@PathVariable(value = "id") Integer filmId
            , @PathVariable Integer userId) throws NotFoundException {
        if (filmId != null && userId != null) {
            return ResponseEntity.ok(filmService.addLike(filmId, userId));
        } else {
            throw new NotFoundException("film id missing");
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    ResponseEntity<Boolean> deletelike(@PathVariable(value = "id") Integer filmId
            , @PathVariable Integer userId) throws NotFoundException {
        if (filmId != null && userId != null) {
            return ResponseEntity.ok(filmService.removeLike(filmId, userId));
        } else {
            throw new NotFoundException("film id or user id missing");
        }
    }

    @GetMapping({"/popular","/popular?count={count}"})
    ResponseEntity<List<Film>> getPopularFilms(@RequestParam(required = false) Integer count) {
        return ResponseEntity.ok(filmService.getPopularFilms(count));
    }
}
