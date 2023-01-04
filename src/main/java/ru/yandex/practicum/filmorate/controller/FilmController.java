package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;
    @PostMapping()
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        films.put(++id, film);
        film.setId(id);
        log.info("film created '{}'", film);
        return ResponseEntity.ok(film);
    }

    @PutMapping()
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            films.put(film.getId(), film);
            log.info("film updated '{}'", film);
            return ResponseEntity.ok(film);
        } else {
            throw new ValidationException("unknown film id");
        }

    }

    //в этом случае ValidationException будет обрабатываться в @RestControllerAdvice
    //ApplicationExceptionsHandler
    @GetMapping()
    public ResponseEntity<List<Film>> getAllFilms() throws ValidationException {
        if (films.isEmpty()) {
            throw new ValidationException("в библиотеке нет фильмов");
        }
        log.info("films size '{}'", films.size());
        return ResponseEntity.ok(films.values().stream().collect(Collectors.toList()));
    }
}
